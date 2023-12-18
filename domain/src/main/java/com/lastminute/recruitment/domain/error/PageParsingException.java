package com.lastminute.recruitment.domain.error;

public class PageParsingException extends RuntimeException {

    public PageParsingException(String message) {
        super(message);
    }

    public PageParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
