package com.example.demo.exception;

public class ZeroBaseException extends RuntimeException {

    private final ExceptionCode code;

    public ZeroBaseException(ExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ZeroBaseException(ExceptionCode code, String message) {
        super(message);
        this.code = code;
    }
}
