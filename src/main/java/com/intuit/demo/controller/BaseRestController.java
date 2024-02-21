package com.intuit.demo.controller;

import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.exception.ApiFailureException;
import com.intuit.demo.exception.UnauthorisedException;
import com.intuit.demo.exception.ValidationFailureException;
import com.intuit.demo.model.response.APIResponse;
import com.intuit.demo.model.response.Error;
import com.intuit.demo.model.response.ResponseBaseModel;
import com.intuit.demo.util.APIResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BaseRestController {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <K extends ResponseBaseModel> APIResponse<K> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<Error> errorList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((validationError) ->
                errorList.add(new Error(((FieldError) validationError).getField(),
                        validationError.getDefaultMessage())));
        return APIResponseUtil.createFailureBadRequestAPIResponse(errorList, null);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorisedException.class)
    public <K extends ResponseBaseModel> APIResponse<K> handleUnauthorisedExceptions(
            UnauthorisedException e) {
        log.error("Exception occurred",e);
        return APIResponseUtil.createFailureUnauthorisedAPIResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public <K extends ResponseBaseModel> APIResponse<K> handleOtherExceptions(
            Throwable e) {
        log.error("Exception occurred",e);
        return APIResponseUtil.createFailureInternalErrorResponse(ErrorConstant.INTERNAL_ERROR);
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ValidationFailureException.class)
    public <K extends ResponseBaseModel> APIResponse<K> handleValidationException
            (ValidationFailureException e) {
        return APIResponseUtil.createFailureBadRequestAPIResponse(e.getErrors(), null);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = WebExchangeBindException.class)
    public <K extends ResponseBaseModel> APIResponse<K> handleValidationExceptionWebBind
            (WebExchangeBindException e) {
        List<Error> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(new Error(error.getField(),error.getDefaultMessage()));
        }

        return APIResponseUtil.createFailureBadRequestAPIResponse(errors, null);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ApiFailureException.class)
    public <K extends ResponseBaseModel> APIResponse<K> handleOtherExceptions(
            ApiFailureException e) {
        log.error("Exception occurred",e);
        return APIResponseUtil.createFailureInternalErrorResponse(e.getMessage());
    }
}
