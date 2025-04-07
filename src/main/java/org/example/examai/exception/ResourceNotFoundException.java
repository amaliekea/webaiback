package org.example.examai.exception;

//ResourceNotFoundException bruges til at signalere en 404-fejl (not found) i din backend.
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
