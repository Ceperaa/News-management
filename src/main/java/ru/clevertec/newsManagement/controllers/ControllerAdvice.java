package ru.clevertec.newsManagement.controllers;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(value = {
            Exception.class
    })
    public ResponseEntity<ExceptionObject> response400(@RequestBody Exception e) {
        ExceptionObject aggregate = aggregate(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(aggregate,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            AuthorizationServiceException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<ExceptionObject> response403(@RequestBody Exception e) {
        ExceptionObject aggregate = aggregate(e.getMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(aggregate,
                HttpStatus.FORBIDDEN);
    }

    private ExceptionObject aggregate(String message, HttpStatus status) {
        return   ExceptionObject.builder()
                .code(status.value())
                .status(String.valueOf(status))
                .message(message).build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class ExceptionObject {

        private int code;
        private String status;
        private String message;
    }
}
