package dev.amir.resourceservice.framework.input.rest.handler;

import dev.amir.resourceservice.domain.exception.InvalidResourceException;
import dev.amir.resourceservice.domain.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exception) {
        String errorMessage = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.error(errorMessage, exception);
        return new ResponseEntity<>("An internal server error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({InvalidResourceException.class})
    public ResponseEntity<String> handleInvalidResourceException(InvalidResourceException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>("Validation failed or request body is invalid MP3", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception) {
        log.warn(exception.getMessage(), exception);
        return new ResponseEntity<>("The resource with the specified id does not exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>("An internal server error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

