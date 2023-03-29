package ru.clevertec.newsManagement.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.clevertec.newsManagement.exception.EntityNotFoundException;

import javax.xml.bind.ValidationException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(value = {
            JsonParseException.class,
            JsonMappingException.class
    })
    public ResponseEntity<ExceptionObject> response400(Exception e) {
        return new ResponseEntity<>(aggregate(e, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            AuthorizationServiceException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<ExceptionObject> response403(Exception e) {
        return new ResponseEntity<>(aggregate(e, HttpStatus.FORBIDDEN),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {
            BadCredentialsException.class,
    })

    public ResponseEntity<ExceptionObject> response401(Exception e) {
        return new ResponseEntity<>(aggregate(e, HttpStatus.UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {
            EntityNotFoundException.class
    })
    public ResponseEntity<ExceptionObject> response404(Exception e) {
        return new ResponseEntity<>(aggregate(e, HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            ExpiredJwtException.class,
            ConstraintViolationException.class,
            ValidationException.class,
            PropertyReferenceException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ExceptionObject> response422(Exception e) {
        return new ResponseEntity<>(aggregate(e, HttpStatus.UNPROCESSABLE_ENTITY),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private ExceptionObject aggregate(Exception e, HttpStatus status) {
        return ExceptionObject.builder()
                .code(status.value())
                .status(String.valueOf(status))
                .message(e.getMessage()).build();
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
