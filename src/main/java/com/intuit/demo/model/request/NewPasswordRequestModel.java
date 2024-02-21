package com.intuit.demo.model.request;

import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.model.request.RequestBaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class NewPasswordRequestModel extends RequestBaseModel {

    @Length(min = AppConstant.MIN_PASSWORD_LENGTH, message = ErrorConstant.INVALID_PASSWORD_LENGTH)
    private String newPassword;

    @NotBlank(message = ErrorConstant.TOKEN_EMPTY)
    private String sha256Token;
}
