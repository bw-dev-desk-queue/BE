package com.lambdaschool.devdesk.queue.models;

import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class FieldErrorDetails {
    private final int status;
    private final String message;
    private List<FieldError> fieldErrors = new ArrayList<>();

    public FieldErrorDetails(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public void addFieldError(String path, String  message)
    {
        fieldErrors.add(new FieldError(path, message, message));
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
