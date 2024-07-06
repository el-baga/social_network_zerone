package com.skillbox.zerone.aop;

import com.skillbox.zerone.dto.request.LoginRq;
import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.repository.PersonRepository;
import com.skillbox.zerone.service.PersonService;
import com.skillbox.zerone.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class PersonStatusAspect {

    private final PersonService personService;
    private final PersonRepository personRepository;

    @Pointcut("@annotation(com.skillbox.zerone.aop.OnlineStatusNoAuth)")
    private void onlineNoAuth() {
    }

    @Pointcut("@annotation(com.skillbox.zerone.aop.OnlineStatus)")
    private void online() {
    }

    @Pointcut("@annotation(com.skillbox.zerone.aop.OfflineStatus)")
    private void offline() {
    }

    @Before("onlineNoAuth()")
    public void setOnlineStatusNoAuth(JoinPoint joinPoint) {
        LoginRq loginRq = (LoginRq) Arrays.stream(joinPoint.getArgs()).
                findFirst().
                orElseThrow(
                        () -> new BadRequestException("No args found in method: " + joinPoint.getSignature().toString())
                );
        final Person person = personService.getByEmail(loginRq.getEmail());
        person.setOnlineStatus(true);
        person.setLastOnlineTime(LocalDateTime.now());
        personRepository.save(person);
    }

    @Before("online()")
    public void setOnlineStatus() {
        final Person person = personService.getPersonById(CommonUtil.getCurrentUserId());
        person.setOnlineStatus(true);
        person.setLastOnlineTime(LocalDateTime.now());
        personRepository.save(person);
    }

    @Before("offline()")
    public void setOfflineStatus() {
        final Person person = personService.getPersonById(CommonUtil.getCurrentUserId());
        person.setOnlineStatus(false);
        person.setLastOnlineTime(LocalDateTime.now());
        personRepository.save(person);
    }
}
