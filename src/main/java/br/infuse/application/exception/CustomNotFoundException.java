package br.infuse.application.exception;

public class CustomNotFoundException extends RuntimeException {
    private static final long serialVerisionUID = 1;

    public CustomNotFoundException(String message) {
        super(message);
    }
}
