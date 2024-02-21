package com.intuit.demo.model.request;


import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.model.request.RequestBaseModel;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequestModel extends RequestBaseModel {

    @Email(regexp = AppConstant.EMAIL_REGEX, message = ErrorConstant.EMAIL_NOT_VALID)
    private String email;

    @NotBlank(message = ErrorConstant.PASSWORD_EMPTY)
    private String password;

}
