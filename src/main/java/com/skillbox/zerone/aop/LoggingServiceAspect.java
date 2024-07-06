package com.skillbox.zerone.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@ConditionalOnProperty(name = "logging.service.enable", matchIfMissing = true)
public class LoggingServiceAspect {

    @Pointcut("@within(com.skillbox.zerone.aop.LoggableDebug)")
    private void loggingService() {
    }

    @AfterThrowing(pointcut = "loggingService()", throwing = "e")
    public void exceptionLogging(JoinPoint joinPoint, Exception e) {
        log.error("Exception in {}.{}() with args {}, exception: {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), joinPoint.getArgs(), e);
    }

    @Around("loggingService()")
    public Object aroundLoggingService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.debug("Call service method {}() with arguments {}", proceedingJoinPoint.getSignature().getName(),
                proceedingJoinPoint.getArgs());
        Object result = proceedingJoinPoint.proceed();
        log.debug("Method {}() has been executed with args {}", proceedingJoinPoint.getSignature().getName(),
                proceedingJoinPoint.getArgs());
        return result;
    }
}
