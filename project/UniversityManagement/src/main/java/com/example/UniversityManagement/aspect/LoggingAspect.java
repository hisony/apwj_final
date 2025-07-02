package com.example.UniversityManagement.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("(execution(* com.example.UniversityManagement.api.*.*(..)) || execution(* com.example.UniversityManagement.services.*.*(..)) || execution(* com.example.UniversityManagement.repository.*.*(..))) && !execution(* com.example.UniversityManagement.api.UserApi.login(..))")
    public void applicationMethod() {}


    @Before("applicationMethod()")
    public void logBefore(JoinPoint joinPoint) {
        String username = getCurrentUsername();
        String methodName = joinPoint.getSignature().getName();
        LocalDateTime timestamp = LocalDateTime.now();

        logger.info("User: {}, Accessed Method: {}, Timestamp: {}", username, methodName, timestamp);
    }

    @AfterReturning("applicationMethod()")
    public void logAfter(JoinPoint joinPoint) {
        String username = getCurrentUsername();
        String methodName = joinPoint.getSignature().getName();
        LocalDateTime timestamp = LocalDateTime.now();

        logger.info("User: {}, Successfully Executed Method: {}, Timestamp: {}", username, methodName, timestamp);
    }

    @AfterThrowing(pointcut = "applicationMethod()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String username = getCurrentUsername();
        String methodName = joinPoint.getSignature().getName();
        LocalDateTime timestamp = LocalDateTime.now();

        logger.error("User: {}, Exception in Method: {}, Timestamp: {}, Exception: {}", username, methodName, timestamp, exception.getMessage());
    }

    private String getCurrentUsername() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getUsername();
        } catch (Exception e) {
            return "Anonymous";
        }
    }
}
