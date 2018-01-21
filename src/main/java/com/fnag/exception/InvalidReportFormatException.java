package com.fnag.exception;

public class InvalidReportFormatException extends RuntimeException {

    public InvalidReportFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
