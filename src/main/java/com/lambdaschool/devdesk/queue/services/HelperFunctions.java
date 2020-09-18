package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.models.ValidationError;

import java.util.List;

public interface HelperFunctions {

    List<ValidationError> getConstraintViolation(Throwable cause);

}
