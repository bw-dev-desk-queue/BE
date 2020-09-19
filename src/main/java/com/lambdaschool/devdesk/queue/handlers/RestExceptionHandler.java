package com.lambdaschool.devdesk.queue.handlers;

import com.lambdaschool.devdesk.queue.exceptions.ResourceFoundException;
import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.ErrorDetail;
import com.lambdaschool.devdesk.queue.services.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    HelperFunctions helperFunctions;

    public RestExceptionHandler() {
        super();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex)
    {
        var error = new ErrorDetail();
        error.setTimestamp(new Date());
        error.setDetail(ex.getMessage());
        error.setDeveloperMessage(ex.getClass().getName());
        error.setErrors(helperFunctions.getConstraintViolation(ex));
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setTitle("RESOURCE NOT FOUND");
        return new ResponseEntity<>(error, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceFoundException.class)
    public ResponseEntity<?> handleResourceFoundException(ResourceFoundException ex)
    {
        var error = new ErrorDetail();
        error.setErrors(helperFunctions.getConstraintViolation(ex));
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setTimestamp(new Date());
        error.setTitle("UNEXPECTED RESOURCE");
        error.setDeveloperMessage(ex.getClass().getName());
        return new ResponseEntity<>(error, null, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var error = new ErrorDetail();
        error.setTitle("An error has occured");
        error.setTimestamp(new Date());
        error.setErrors(helperFunctions.getConstraintViolation(ex));
        error.setDeveloperMessage(ex.getClass().getName());
        error.setStatus(status.value());

        return new ResponseEntity<>(error, null, status);
    }
}
