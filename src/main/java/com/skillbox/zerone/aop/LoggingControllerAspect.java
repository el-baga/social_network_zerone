package com.skillbox.zerone.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@ConditionalOnProperty(name = "logging.controller.enable", matchIfMissing = true)
public class LoggingControllerAspect {

    @Pointcut("@within(com.skillbox.zerone.aop.LoggableInfo)")
    private void loggingController() {
    }

    @AfterThrowing(pointcut = "loggingController()", throwing = "e")
    public void exceptionLogging(JoinPoint joinPoint, Exception e) {
        log.error("Exception in {}.{}() with args {}, exception: {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), joinPoint.getArgs(), e);
    }

    @Before("loggingController()")
    public void requestOnController(JoinPoint joinPoint) {
        log.info("User send request to method {}() with arguments {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "loggingController()", returning = "result")
    public void responseFromController(JoinPoint joinPoint, Object result) {
        log.info("Response {} from controller {}()", result, joinPoint.getSignature().getName());
    }
}
