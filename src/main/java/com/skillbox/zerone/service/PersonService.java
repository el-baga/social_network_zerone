package com.skillbox.zerone.service;

import com.skillbox.zerone.aop.LoggableDebug;
import com.skillbox.zerone.dto.request.*;
import com.skillbox.zerone.dto.response.*;
import com.skillbox.zerone.entity.*;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.mapper.PersonMapper;
import com.skillbox.zerone.mapper.WeatherMapper;
import com.skillbox.zerone.message.MessageType;
import com.skillbox.zerone.repository.PersonRepository;
import com.skillbox.zerone.repository.PersonSettingsRepository;
import com.skillbox.zerone.security.JwtTokenUtils;
import com.skillbox.zerone.specification.Specs;
import com.skillbox.zerone.util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final CaptchaService captchaService;

    private final PersonRepository personRepository;

    private final PersonSettingsRepository personSettingsRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtils jwtTokenUtils;

    private final JavaMailSender javaMailSender;

    private final GeolocationService geolocationService;

    private final CurrencyService currencyService;

    private final WeatherService weatherService;

    @Value("${default.image}")
    private String defaultImage;

    @Value("${spring.base-url}")
    private String baseUrl;

    @Value("${spring.mail.login}")
    private String from;

    @Value("${spring.mail.personal}")
    private String personal;

    @Transactional
    @LoggableDebug
    public RegisterRs create(RegisterRq registerRq) {
        if (isExistPerson(registerRq)) {
            throw new BadRequestException("Пользователь с таким email уже существует");
        }
        if (!registerRq.getPassword().equals(registerRq.getConfirmPassword())) {
            throw new BadRequestException("Пароли должны совпадать");
        }
        captchaService.checkCaptcha(registerRq);
        Person person = PersonMapper.INSTANCE.toEntity(registerRq);
        setBaseValue(person);
        PersonSettings personSettings = new PersonSettings();
        person.setPersonSettings(personSettings);
        person.setPhoto(defaultImage);
        personSettingsRepository.save(personSettings);
        personRepository.save(person);
        return RegisterRs.builder()
                .data(new ComplexRs())
                .email(person.getEmail())
                .build();
    }

    @LoggableDebug
    public PageRs<PersonRs> login(LoginRq loginRequest) {
        final Person person = getByEmail(loginRequest.getEmail());
        final boolean isBlocked = person.getIsBlocked();
        if (isBlocked) {
            throw new BadRequestException("Ваш аккаунт заблокирован");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), person.getPassword())) {
            throw new BadRequestException("Введены некорректные данные");
        }
        person.setOnlineStatus(true);
        final PersonRs personRs = PersonMapper.INSTANCE.toDto(person);
        personRs.setToken(jwtTokenUtils.generateToken(person));
        personRs.setFriendStatus("");
        personRs.setIsBlockedByCurrentUser(false);
        if (!Objects.isNull(loginRequest.getChatId())) {
            person.setTelegramId(loginRequest.getChatId());
        }
        updatePerson(person);
        return PageRs.<PersonRs>builder()
                .data(personRs)
                .build();
    }

    @LoggableDebug
    public PageRs<ComplexRs> logout() {
        Long id = CommonUtil.getCurrentUserId();
        Person person = getPersonById(id);
        person.setOnlineStatus(false);
        person.setLastOnlineTime(LocalDateTime.now());
        updatePerson(person);
        SecurityContextHolder.clearContext();
        return PageRs.<ComplexRs>builder()
                .build();
    }

    @LoggableDebug
    public PageRs<PersonRs> aboutMe() {
        Long id = CommonUtil.getCurrentUserId();
        Person person = getPersonById(id);
        PersonRs personRs = PersonMapper.INSTANCE.toDto(person);
        personRs.setFriendStatus(FriendshipStatus.UNKNOWN.name());
        personRs.setUserDeleted(person.getIsDeleted());
        personRs.setMessagePermission("ALL");
        personRs.setCurrency(createCurrencyRs());
        if (person.getCity() != null && weatherService.getWeather(personRs.getCity()) != null) {
            personRs.setWeather(createWeatherRs(person.getCity()));
        }
        return PageRs.<PersonRs>builder()
                .data(personRs)
                .build();
    }

    private CurrencyRs createCurrencyRs() {
        CurrencyRs currencyRs = new CurrencyRs();
        currencyRs.setEuro(currencyService.getCurrency("Евро"));
        currencyRs.setUsd(currencyService.getCurrency("Доллар США"));
        return currencyRs;
    }

    public WeatherRs createWeatherRs(String city) {
        Weather weather = weatherService.getWeather(city);
        return WeatherMapper.INSTANCE.weatherToWeatherDTO(weather);
    }

    public void updatePerson(Person person) {
        personRepository.save(person);
    }

    public Person getByEmail(String email) {
        return personRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("Введены некорректные данные"));
    }

    private boolean isExistPerson(RegisterRq person) {
        return personRepository.findByEmail(person.getEmail()).isPresent();
    }

    private void setBaseValue(Person person) {
        String password = passwordEncoder.encode(person.getPassword());
        person.setPassword(password);
        person.setOnlineStatus(false);
        person.setIsBlocked(false);
        person.setIsDeleted(false);
        person.setMessagePermissions("ALL");
    }

    public Person getPersonById(long id) {
        return personRepository.findById(id).orElseThrow(() -> new BadRequestException("User with userName "
                + id + " not found"));
    }

    public PageRs<List<PersonRs>> searchPersons(PersonSearchRq req) {
        final Page<Person> page = personRepository.findAll(
                Specification
                        .where(Specs.eq(Person_.isDeleted, false))
                        .and(Specs.like(Person_.firstName, req.getFirstName()))
                        .and(Specs.like(Person_.lastName, req.getLastName()))
                        .and(Specs.eq(Person_.country, req.getCountry()))
                        .and(Specs.eq(Person_.city, req.getCity()))
                        .and(Specs.lessEq(Person_.birthDate, req.getAgeFrom()))
                        .and(Specs.greaterEq(Person_.birthDate, req.getAgeTo()))
                        .and(Specs.notEq(Person_.id, CommonUtil.getCurrentUserId())),
                PageRequest.of(
                        CommonUtil.offsetToPageNum(req.getOffset(), req.getPerPage()),
                        req.getPerPage(),
                        Sort.by(Sort.Direction.ASC, Person_.LAST_NAME, Person_.FIRST_NAME)
                )
        );

        return PageRs
                .<List<PersonRs>>builder()
                .data(page.getContent().stream().map(PersonMapper.INSTANCE::toDto).toList())
                .itemPerPage(page.getNumberOfElements())
                .offset(req.getOffset())
                .perPage(req.getPerPage())
                .total(page.getTotalElements())
                .build();
    }

    @Transactional
    public PageRs<PersonRs> updateUserInfo(PersonRq personRq) {
        Long id = CommonUtil.getCurrentUserId();
        Person person = getPersonById(id);
        Person updatedPerson = PersonMapper.INSTANCE.toEntity(personRq, person);
        geolocationService.addCityToDb(personRq);
        updatePerson(updatedPerson);
        if (personRq.getCity() != null) {
            weatherService.getWeatherData(personRq.getCity());
        }
        PersonRs personRs = PersonMapper.INSTANCE.toDto(updatedPerson);
        return PageRs.<PersonRs>builder()
                .data(personRs)
                .build();
    }

    public PageRs<PersonRs> getPersonProfileById(Long dstId) {
        Long srcId = CommonUtil.getCurrentUserId();
        Person srcPerson = getPersonById(srcId);
        Person dstPerson = getPersonById(dstId);
        PersonRs personRs = PersonMapper.INSTANCE.toDto(dstPerson);
        String status = getFriendshipsStatus(dstId, srcPerson);
        personRs.setFriendStatus(status);
        return PageRs.<PersonRs>builder()
                .data(personRs)
                .total(1L)
                .build();
    }

    public List<Person> getPersonsByBirthDate(LocalDate birthDate) {
        return personRepository.findAll(
                Specification
                        .where(Specs.eq(Person_.isDeleted, false))
                        .and(Specs.eq(Person_.isBlocked, false))
                        .and(Specs.funcEq("date_part", "day", Person_.birthDate, birthDate.getDayOfMonth()))
                        .and(Specs.funcEq("date_part", "month", Person_.birthDate, birthDate.getMonthValue()))
        );
    }

    private String getFriendshipsStatus(Long id, Person srcPerson) {
        List<Friendships> friendshipsList = srcPerson.getSrcFriends();
        String status = FriendshipStatus.UNKNOWN.name();
        if (!friendshipsList.isEmpty()) {
            friendshipsList.removeIf(friendships -> !friendships.getDstPersonId().getId().equals(id));
            status = friendshipsList.isEmpty() ? FriendshipStatus.UNKNOWN.name() : friendshipsList.get(0).getStatusName();
        }
        return status;
    }

    public void changePersonEmail(String personEmail, Long userId) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");
            messageHelper.setFrom(from, personal);
            messageHelper.setTo(personEmail);
            messageHelper.setSubject(MessageType.RESET_EMAIL_SUBJECT.getText());
            String link = getEmailResetLink(userId);
            String content = getEmailResetContent(link);
            messageHelper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BadRequestException("Something went wrong with sending email recovery letter on email.");
        }
    }

    private String getEmailResetLink(Long id) {
        Person person = getPersonById(id);
        String code = UUID.randomUUID().toString();
        person.setEmailUUIDTime(LocalDateTime.now());
        person.setEmailUUID(code);
        updatePerson(person);
        return String.format(MessageType.RESET_EMAIL_FORMAT_LINK.getText(), baseUrl, code);
    }

    private String getEmailResetContent(String link) {
        return "<p>Здравствуйте,</p>"
                + "<p>С вашего аккаунта был отправлен запрос на изменение текущей почты. </p>"
                + "<p>Перейдите по ссылке для изменения почты:</p>"
                + "<p>" + link + "</p>"
                + "<br>"
                + "<p>Если вы не отправляли запрос на изменение почты, то проигнорируйте данное сообщение.</p>";
    }

    public RegisterRs setPersonEmail(EmailRq email) {
        Optional<Person> personOptional = personRepository.findPersonByEmailUUID(email.getSecret());
        if (personOptional.isEmpty()) {
            throw new BadRequestException(MessageType.EMAIL_UUID_IS_NOT_FOUND_ERROR.getText());
        }

        Person person = personOptional.get();
        LocalDateTime emailUUIDTime = person.getEmailUUIDTime();
        LocalDateTime currentTime = LocalDateTime.now();
        boolean isTimeDiffMoreThanTenMin = Duration.between(emailUUIDTime, currentTime).toMinutes() > 10L;
        if (isTimeDiffMoreThanTenMin) {
            throw new BadRequestException(MessageType.EMAIL_UUID_EXPIRED_ERROR.getText());
        }

        if (personRepository.existsByEmail(email.getEmail())) {
            throw new BadRequestException(MessageType.EMAIL_IS_ALREADY_TAKEN_ERROR.getText());
        }

        person.setEmail(email.getEmail());
        updatePerson(person);
        return RegisterRs.builder()
                .data(new ComplexRs())
                .email(email.getEmail())
                .build();
    }

    public RegisterRs setPersonPassword(PasswordSetRq passwordSetRq) {
        Long id = CommonUtil.getCurrentUserId();
        Person person = getPersonById(id);
        String password = passwordEncoder.encode(passwordSetRq.getPassword());
        person.setPassword(password);
        updatePerson(person);
        return RegisterRs.builder()
                .data(new ComplexRs())
                .email(person.getEmail())
                .build();
    }

    public RegisterRs resetPersonPassword(PasswordResetRq passwordResetRq) {
        Optional<Person> personOptional = personRepository.findPersonByChangePasswordToken(passwordResetRq.getSecret());
        if (personOptional.isEmpty()) {
            throw new BadRequestException(MessageType.PASSWORD_TOKEN_IS_NOT_FOUND_ERROR.getText());
        }

        Person person = personOptional.get();
        LocalDateTime changePasswordTokenTime = person.getChangePasswordTokenTime();
        LocalDateTime currentTime = LocalDateTime.now();
        boolean isTimeDiffMoreThanTenMin = Duration.between(changePasswordTokenTime, currentTime).toMinutes() > 10L;
        if (isTimeDiffMoreThanTenMin) {
            throw new BadRequestException(MessageType.PASSWORD_TOKEN_EXPIRED_ERROR.getText());
        }

        String password = passwordEncoder.encode(passwordResetRq.getPassword());
        person.setPassword(password);
        updatePerson(person);
        return RegisterRs.builder()
                .data(new ComplexRs())
                .email(person.getEmail())
                .build();
    }

    public void recoverPersonPassword(String email, Long userId) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");
            messageHelper.setFrom(from, personal);
            messageHelper.setTo(email);
            messageHelper.setSubject(MessageType.RESET_PASSWORD_SUBJECT.getText());
            String link = getPasswordResetLink(userId);
            String content = getPasswordResetContent(link);
            messageHelper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BadRequestException("Something went wrong with sending password recovery letter on email.");
        }
    }

    private String getPasswordResetLink(Long id) {
        Person person = getPersonById(id);
        String code = UUID.randomUUID().toString();
        person.setChangePasswordToken(code);
        person.setChangePasswordTokenTime(LocalDateTime.now());
        updatePerson(person);
        return String.format(MessageType.RESET_PASSWORD_FORMAT_LINK.getText(), baseUrl, code);
    }

    private String getPasswordResetContent(String link) {
        return "<p>Здравствуйте,</p>"
                + "<p>С вашего аккаунта был отправлен запрос на изменение текущего пароля. </p>"
                + "<p>Перейдите по ссылке для изменения пароля:</p>"
                + "<p>" + link + "</p>"
                + "<br>"
                + "<p>Если вы не отправляли запрос на изменение пароля, то проигнорируйте данное сообщение.</p>";
    }

    public PageRs<ComplexRs> deleteInfoAboutMe() {
        Long id = CommonUtil.getCurrentUserId();
        Person person = getPersonById(id);
        person.setIsDeleted(true);
        person.setDeletedTime(LocalDateTime.now());
        updatePerson(person);
        return PageRs.<ComplexRs>builder()
                .data(new ComplexRs())
                .build();
    }


    public PageRs<ComplexRs> recoverInfoAboutMe() {
        Long id = CommonUtil.getCurrentUserId();
        Person person = getPersonById(id);
        person.setIsDeleted(false);
        person.setDeletedTime(null);
        updatePerson(person);
        return PageRs.<ComplexRs>builder()
                .data(new ComplexRs())
                .build();
    }

    public Set<String> getCities() {
        List<Person> persons = personRepository.findAll(
                Specification.where(Specs.eq(Person_.isBlocked, false))
                        .and(Specs.eq(Person_.isDeleted, false))
                        .and(Specs.isNotNull(Person_.city))
        );
        return persons.stream()
                .map(Person::getCity)
                .collect(Collectors.toSet());
    }
}
