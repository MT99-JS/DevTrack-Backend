package com.devtrack.devtrack_backend.exception;

public class DuplicateProjectKeyException extends RuntimeException {

    public DuplicateProjectKeyException(String message) {
        super(message);
    }
}
