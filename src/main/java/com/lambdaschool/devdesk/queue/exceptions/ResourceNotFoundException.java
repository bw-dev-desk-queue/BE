package com.lambdaschool.devdesk.queue.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(String.format("Error: %s", message));
    }
}
