package com.lambdaschool.devdesk.queue.models;

public class ValidationError {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("Validation Error{code=%s, message=%s}", this.code, this.message);
    }
}
