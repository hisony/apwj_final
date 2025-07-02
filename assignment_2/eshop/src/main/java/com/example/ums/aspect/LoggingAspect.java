package com.example.ums.aspect;

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

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);  // Corrected class name

    // Pointcut to capture execution of methods in the controller, service, and repository layers
    @Pointcut("execution(* com.example.ums.api.AdminApi.*(..)) || execution(* com.example.ums.service.*.*(..))")
    public void applicationMethod() {}

    // Before method execution: Log the username, method name, and timestamp before execution
    @Before("applicationMethod()")
    public void logBefore(JoinPoint joinPoint) {
        String username = getCurrentUsername();
        String methodName = joinPoint.getSignature().getName();
        LocalDateTime timestamp = LocalDateTime.now();

        logger.info("User: {}, Accessed Method: {}, Timestamp: {}", username, methodName, timestamp);
    }

    // After method execution: Log the username, method name, and timestamp after execution
    @AfterReturning("applicationMethod()")
    public void logAfter(JoinPoint joinPoint) {
        String username = getCurrentUsername();
        String methodName = joinPoint.getSignature().getName();
        LocalDateTime timestamp = LocalDateTime.now();

        logger.info("User: {}, Successfully Executed Method: {}, Timestamp: {}", username, methodName, timestamp);
    }

    // After throwing an exception: Log when an exception is thrown in any method
    @AfterThrowing(pointcut = "applicationMethod()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String username = getCurrentUsername();
        String methodName = joinPoint.getSignature().getName();
        LocalDateTime timestamp = LocalDateTime.now();

        logger.error("User: {}, Exception in Method: {}, Timestamp: {}, Exception: {}", username, methodName, timestamp, exception.getMessage());
    }

    // Helper method to get the current logged-in user's username
    private String getCurrentUsername() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getUsername();
        } catch (Exception e) {
            return "Anonymous"; // If the user is not authenticated, return "Anonymous"
        }
    }
}




