package com.gab.springboot.bankingapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class InsightUnavailableException extends RuntimeException {
    public InsightUnavailableException() {
        super("Insights are temporarily unavailable. Please try again later.");
    }
}
