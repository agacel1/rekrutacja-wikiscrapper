package com.lastminute.recruitment.domain.error;

public class WikiPageNotFound extends RuntimeException {

    public WikiPageNotFound(String message) {
        super(message);
    }
}
