package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.models.FieldErrorDetails;
import com.lambdaschool.devdesk.queue.models.ValidationError;
import org.springframework.validation.FieldError;

import java.util.List;

public interface HelperFunctions {

    List<ValidationError> getConstraintViolation(Throwable cause);
    FieldErrorDetails processFieldErrors(List<FieldError> fieldErrors);
    List<ValidationError> fieldErrorDetailsToValidationErrors(FieldErrorDetails details);

    boolean isAuthorizedToMakeChange(String username);

}
