package com.blankerdog.carService.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.apache.tomcat.websocket.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorInfo authenticationExceptionHandler(HttpServletRequest request, AuthenticationException exception){
        return new ErrorInfo(401, "UNAUTHORIZED", exception.getMessage(), UrlUtils.buildFullRequestUrl(request));
    }

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorInfo authenticationExceptionHandler(HttpServletRequest request, TokenRefreshException exception){
        return new ErrorInfo(403, "FORBIDDEN", exception.getMessage(), UrlUtils.buildFullRequestUrl(request));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorInfo entityNotFoundExceptionHandler(HttpServletRequest request, EntityNotFoundException exception){
        return new ErrorInfo(404, "NOT_FOUND", exception.getMessage(), UrlUtils.buildFullRequestUrl(request));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorInfo accessDeniedExceptionHandler(HttpServletRequest request, AccessDeniedException exception){
        return new ErrorInfo(403, "FORBIDDEN", exception.getMessage(), UrlUtils.buildFullRequestUrl(request));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorInfo missingServletRequestParameterExceptionHandler(HttpServletRequest request, MissingServletRequestParameterException exception){
        return new ErrorInfo(400, "BAD_REQUEST", exception.getMessage(), UrlUtils.buildFullRequestUrl(request));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorInfo internalServerExceptionHandler(HttpServletRequest request, Exception exception){
        return new ErrorInfo(500, "INTERNAL_SERVER_ERROR", exception.getMessage(), UrlUtils.buildFullRequestUrl(request));
    }

    @Getter
    public class ErrorInfo {
        private final LocalDateTime timestamp;
        private final Integer status;
        private final String error;
        private final String message;
        private final String path;


        public ErrorInfo(Integer status, String error, String message, String path) {
            this.timestamp = LocalDateTime.now();
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
            logger.error("Exception raised = {} :: URL = {}", message, path);
        }
    }
}
