package com.intuit.demo.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnauthorisedException extends RuntimeException{

    private String message;
    public UnauthorisedException(String message) {
        this.message = message;
    }
}
