package com.intuit.demo.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiFailureException extends RuntimeException{

    private String message;
    public ApiFailureException(String message) {
        this.message = message;
    }
}
