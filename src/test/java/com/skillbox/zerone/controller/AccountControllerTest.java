package com.skillbox.zerone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.zerone.config.TestConfig;
import com.skillbox.zerone.dto.request.*;
import com.skillbox.zerone.entity.NotificationType;
import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.listener.KafkaMessage;
import com.skillbox.zerone.repository.PersonRepository;
import com.skillbox.zerone.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/accountController-beforeTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/accountController-afterTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder encoder;

    @LocalServerPort
    private int port;

    @MockBean
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    private RegisterRq createRegisterRq() {
        RegisterRq registerRq = new RegisterRq();
        registerRq.setFirstName("firstName");
        registerRq.setLastName("lastName");
        registerRq.setEmail("email@mail.com");
        registerRq.setPassword("Password1");
        registerRq.setConfirmPassword("Password1");
        registerRq.setCode("code");
        String encode = Base64.getEncoder().encodeToString(registerRq.getCode().getBytes());
        registerRq.setCodeSecret(encode);
        return registerRq;
    }

    @Test
    @DisplayName("Корректная регистрация пользователя")
    void testRegister_result200() throws Exception {
        String content = new ObjectMapper().writeValueAsString(createRegisterRq());
        mockMvc.perform(post("http://localhost:" + port + "/api/v1/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("email@mail.com"));
    }

    @Test
    @DisplayName("Введены не совпадающие пароли")
    void testRegister_mismatchedConfirmPassword_result400() throws Exception {
        RegisterRq registerRq = createRegisterRq();
        registerRq.setConfirmPassword("Password2");
        String content = new ObjectMapper().writeValueAsString(registerRq);
        mockMvc.perform(post("http://localhost:" + port + "/api/v1/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Введен пустой подтверждающий пароль")
    void testRegister_incorrectConfirmPassword_result400() throws Exception {
        RegisterRq registerRq = createRegisterRq();
        registerRq.setConfirmPassword("");
        String content = new ObjectMapper().writeValueAsString(registerRq);
        mockMvc.perform(post("http://localhost:" + port + "/api/v1/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Введено некорректное имя")
    void testRegister_invalidFirstName_result400() throws Exception {
        RegisterRq registerRq = createRegisterRq();
        registerRq.setFirstName("A");
        String content = new ObjectMapper().writeValueAsString(registerRq);
        mockMvc.perform(post("http://localhost:" + port + "/api/v1/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Введена некорректная фамилия")
    void testRegister_invalidLastName_result400() throws Exception {
        RegisterRq registerRq = createRegisterRq();
        registerRq.setLastName("A");
        String content = new ObjectMapper().writeValueAsString(registerRq);
        mockMvc.perform(post("http://localhost:" + port + "/api/v1/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Задан пароль, несоответствующий требованиям")
    void testRegister_incorrectPassword_result400() throws Exception {
        RegisterRq registerRq = createRegisterRq();
        registerRq.setPassword("password");
        registerRq.setConfirmPassword("password");
        String content = new ObjectMapper().writeValueAsString(registerRq);
        mockMvc.perform(post("http://localhost:" + port + "/api/v1/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Указан некорректный email")
    void testRegister_incorrectEmail_result400() throws Exception {
        RegisterRq registerRq = createRegisterRq();
        registerRq.setEmail("email");
        String content = new ObjectMapper().writeValueAsString(registerRq);
        mockMvc.perform(post("http://localhost:" + port + "/api/v1/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Регистрация пользователя с email, присутствующим в базе данных")
    void testRegister_duplicateEmail_result400() throws Exception {
        RegisterRq registerRq = createRegisterRq();
        registerRq.setEmail("1@1.com");
        String content = new ObjectMapper().writeValueAsString(registerRq);
        mockMvc.perform(post("http://localhost:" + port + "/api/v1/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Введена не верная капча")
    void testRegister_incorrectCaptcha_result400() throws Exception {
        RegisterRq registerRq = createRegisterRq();
        registerRq.setCode("incorrectCaptcha");
        String content = new ObjectMapper().writeValueAsString(registerRq);
        mockMvc.perform(post("http://localhost:" + port + "/api/v1/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Успешное изменение настроек уведомления ответа на комментарий")
    @WithMockUser("1")
    @Transactional
    void testSetNotificationSetting_changeCommentCommentSettings_result200() throws Exception {
        Long id = 1L;
        NotificationSettingRq request = new NotificationSettingRq();
        request.setNotificationType(NotificationType.COMMENT_COMMENT);
        request.setEnable(false);
        String content = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk());
        Person person = personRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Пользователь с id: " + id + " не найден"));
        boolean commentCommentSetting = person.getPersonSettings().getCommentComment();
        Assertions.assertFalse(commentCommentSetting);
    }

    @Test
    @DisplayName("Успешное изменение настроек уведомления день рождения друзей")
    @WithMockUser("1")
    @Transactional
    void testSetNotificationSetting_changeFriendBirthdaySettings_result200() throws Exception {
        Long id = 1L;
        NotificationSettingRq request = new NotificationSettingRq();
        request.setNotificationType(NotificationType.FRIEND_BIRTHDAY);
        request.setEnable(false);
        String content = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk());
        Person person = personRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Пользователь с id: " + id + " не найден"));
        boolean friendBirthdaySetting = person.getPersonSettings().getFriendBirthday();
        Assertions.assertFalse(friendBirthdaySetting);
    }

    @Test
    @DisplayName("Успешное изменение настроек уведомления личные сообщения")
    @WithMockUser("1")
    @Transactional
    void testSetNotificationSetting_changeMessageSettings_result200() throws Exception {
        Long id = 1L;
        NotificationSettingRq request = new NotificationSettingRq();
        request.setNotificationType(NotificationType.MESSAGE);
        request.setEnable(false);
        String content = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk());
        Person person = personRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Пользователь с id: " + id + " не найден"));
        boolean messageSetting = person.getPersonSettings().getMessage();
        Assertions.assertFalse(messageSetting);
    }

    @Test
    @DisplayName("Успешное изменение настроек уведомления публикация постов")
    @WithMockUser("1")
    @Transactional
    void testSetNotificationSetting_changePostSettings_result200() throws Exception {
        Long id = 1L;
        NotificationSettingRq request = new NotificationSettingRq();
        request.setNotificationType(NotificationType.POST);
        request.setEnable(false);
        String content = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk());
        Person person = personRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Пользователь с id: " + id + " не найден"));
        boolean postSetting = person.getPersonSettings().getPost();
        Assertions.assertFalse(postSetting);
    }

    @Test
    @DisplayName("Успешное изменение настроек уведомления комментарий к посту")
    @WithMockUser("1")
    @Transactional
    void testSetNotificationSetting_changePostCommentSettings_result200() throws Exception {
        Long id = 1L;
        NotificationSettingRq request = new NotificationSettingRq();
        request.setNotificationType(NotificationType.POST_COMMENT);
        request.setEnable(false);
        String content = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk());
        Person person = personRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Пользователь с id: " + id + " не найден"));
        boolean postCommentSetting = person.getPersonSettings().getPostComment();
        Assertions.assertFalse(postCommentSetting);
    }

    @Test
    @DisplayName("Успешное изменение настроек уведомления день лайк к посту/комментарию")
    @WithMockUser("1")
    @Transactional
    void testSetNotificationSetting_changePostCommentLikeSettings_result200() throws Exception {
        Long id = 1L;
        NotificationSettingRq request = new NotificationSettingRq();
        request.setNotificationType(NotificationType.POST_LIKE);
        request.setEnable(false);
        String content = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk());
        Person person = personRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Пользователь с id: " + id + " не найден"));
        boolean postLikeSetting = person.getPersonSettings().getPostLike();
        Assertions.assertFalse(postLikeSetting);
    }

    @Test
    @DisplayName("Успешное изменение настроек уведомлений")
    @WithMockUser("1")
    @Transactional
    void testSetNotificationSetting_changeFriendRequestSettings_result200() throws Exception {
        Long id = 1L;
        NotificationSettingRq request = new NotificationSettingRq();
        request.setNotificationType(NotificationType.FRIEND_REQUEST);
        request.setEnable(false);
        String content = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk());
        Person person = personRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Пользователь с id: " + id + " не найден"));
        boolean friendRequestSetting = person.getPersonSettings().getFriendRequest();
        Assertions.assertFalse(friendRequestSetting);
    }

    @Test
    @DisplayName("Изменение настроек уведомлений неавторизованным пользователем")
    @WithAnonymousUser
    void testSetNotificationSetting_anonymous_result401() throws Exception {
        NotificationSettingRq request = new NotificationSettingRq();
        request.setNotificationType(NotificationType.FRIEND_REQUEST);
        request.setEnable(true);
        String content = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Получение настроек уведомлений")
    @WithMockUser("2")
    void testGetNotificationsSettings_getNotificationsSettings_result200() throws Exception {
        mockMvc.perform(get("http://localhost:" + port + "/api/v1/account/notifications"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].enable").value(true))
                .andExpect(jsonPath("$.data[1].enable").value(false))
                .andExpect(jsonPath("$.data[2].enable").value(true))
                .andExpect(jsonPath("$.data[3].enable").value(false))
                .andExpect(jsonPath("$.data[4].enable").value(true))
                .andExpect(jsonPath("$.data[5].enable").value(false))
                .andExpect(jsonPath("$.data[6].enable").value(true));
    }

    @Test
    @DisplayName("Получение настроек уведомлений неавторизованным пользователем")
    @WithAnonymousUser
    void testGetNotificationsSettings_anonymous_result401() throws Exception {
        mockMvc.perform(get("http://localhost:" + port + "/api/v1/account/notifications"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Успешная смена пароля пользователя")
    @WithMockUser("2")
    void testSetPersonPassword_successfulPasswordChange_result200() throws Exception {
        String newPassword = "Password2";
        PasswordSetRq passwordSetRq = new PasswordSetRq();
        passwordSetRq.setPassword(newPassword);
        String content = new ObjectMapper().writeValueAsString(passwordSetRq);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/password/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("2@2.com"));
        Long id = 2L;
        Person person = personRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Пользователь с id: " + id + " не найден"));
        boolean checkPassword = encoder.matches(newPassword, person.getPassword());
        Assertions.assertTrue(checkPassword);
    }

    @Test
    @DisplayName("Попытка смены пароля неавторизованным пользователем")
    @WithAnonymousUser
    void testSetPersonPassword_anonymous_result401() throws Exception {
        String newPassword = "Password2";
        PasswordSetRq passwordSetRq = new PasswordSetRq();
        passwordSetRq.setPassword(newPassword);
        String content = new ObjectMapper().writeValueAsString(passwordSetRq);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/password/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Успешная смена пароля после его сброса")
    @WithMockUser("4")
    void testResetPersonPassword_successfulReset_result200() throws Exception {
        LocalDateTime changePasswordTokenTime = LocalDateTime.now();
        Person person = personService.getPersonById(4L);
        person.setChangePasswordTokenTime(changePasswordTokenTime);
        personRepository.save(person);
        String newPassword = "MyNewPass1";
        PasswordResetRq passwordResetRq = new PasswordResetRq();
        passwordResetRq.setPassword(newPassword);
        passwordResetRq.setSecret("secretForPassword");
        String content = new ObjectMapper().writeValueAsString(passwordResetRq);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk());
        String password = personRepository.findById(4L).orElseThrow().getPassword();
        boolean checkPassword = encoder.matches(newPassword, password);
        Assertions.assertTrue(checkPassword);
    }

    @Test
    @DisplayName("Использование некорректного секрета для смены пароля")
    @WithMockUser("4")
    void testResetPersonPassword_incorrectSecret_result400() throws Exception {
        String newPassword = "MyNewPass1";
        PasswordResetRq passwordResetRq = new PasswordResetRq();
        passwordResetRq.setPassword(newPassword);
        passwordResetRq.setSecret("incorrectSecret");
        String content = new ObjectMapper().writeValueAsString(passwordResetRq);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Успешная смена электронной почты")
    @WithMockUser("3")
    void testSetPersonEmail_changeEmail_result200() throws Exception {
        LocalDateTime emailUUIDTime = LocalDateTime.now();
        Person person = personService.getPersonById(3L);
        person.setEmailUUIDTime(emailUUIDTime);
        personRepository.save(person);
        String newEmail = "new@mail.com";
        EmailRq emailRq = new EmailRq();
        emailRq.setEmail(newEmail);
        emailRq.setSecret("secretForEmail");
        String content = new ObjectMapper().writeValueAsString(emailRq);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk());
        String emailFromDb = personRepository.findById(3L).orElseThrow().getEmail();
        Assertions.assertEquals(newEmail, emailFromDb);
    }

    @Test
    @DisplayName("Использование некорректного секрета для смены электронной почты")
    @WithMockUser("3")
    void testSetPersonEmail_incorrectSecret_result400() throws Exception {
        String newEmail = "new@mail.com";
        EmailRq emailRq = new EmailRq();
        emailRq.setEmail(newEmail);
        emailRq.setSecret("incorrectSecret");
        String content = new ObjectMapper().writeValueAsString(emailRq);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Успешная отправка запроса на изменение электронной почты")
    @WithMockUser("1")
    void testChangePersonEmail_correctRequest_result200() throws Exception {
        String email = "newEmail@mail.com";
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/email/recovery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(email))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Отправка запроса на смену электронной почты неавторизованным пользователем")
    @WithAnonymousUser
    void testChangePersonEmail_anonymous_result401() throws Exception {
        String email = "newEmail@mail.com";
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/email/recovery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(email))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Успешная отправка запроса на изменение электронной почты")
    @WithMockUser("1")
    void testRecoverPersonPassword_correctRequest_result200() throws Exception {
        PasswordRecoveryRq passwordRecoveryRq = new PasswordRecoveryRq();
        passwordRecoveryRq.setEmail("1@1.com");
        String content = new ObjectMapper().writeValueAsString(passwordRecoveryRq);
        mockMvc.perform(put("http://localhost:" + port + "/api/v1/account/password/recovery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
