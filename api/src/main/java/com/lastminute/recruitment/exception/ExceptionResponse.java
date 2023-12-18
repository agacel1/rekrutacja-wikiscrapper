package com.lastminute.recruitment.exception;

import java.util.List;

public record ExceptionResponse(String message, List<String>details) {
}
