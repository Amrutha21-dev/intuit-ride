package com.intuit.demo.exception;

import com.intuit.demo.model.response.Error;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationFailureException extends RuntimeException{

    private List<Error> errors;
    public ValidationFailureException(List<Error> errors) {
        this.errors = errors;
    }
}
