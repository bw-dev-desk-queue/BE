package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.models.ValidationError;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Service(value = "helperFunctions")
public class HelperFunctionsImpl implements HelperFunctions{
    @Override
    public List<ValidationError> getConstraintViolation(Throwable cause) {
        while((cause != null) && !(cause instanceof ConstraintViolationException))
        {
            cause = cause.getCause();
        }
        List<ValidationError> errors = new ArrayList<>();
        if(cause != null)
        {
            var exception = (ConstraintViolationException) cause;
            for(ConstraintViolation cvException : exception.getConstraintViolations())
            {
                ValidationError err = new ValidationError();
                err.setCode(cvException.getInvalidValue().toString());
                err.setMessage(cvException.getMessage());
                errors.add(err);
            }
        }
        return errors;
    }
}
