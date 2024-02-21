package com.intuit.demo.model.request;

import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.model.request.RequestBaseModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
public class UserRequestModel extends RequestBaseModel {

    @NotBlank(message = ErrorConstant.FIRST_NAME_EMPTY)
    private String firstName;

    @NotBlank(message = ErrorConstant.LAST_NAME_EMPTY)
    private String lastName;

    @Email(regexp = AppConstant.EMAIL_REGEX, message = ErrorConstant.EMAIL_NOT_VALID)
    private String email;

    @Length(min = AppConstant.MIN_PASSWORD_LENGTH,message = ErrorConstant.INVALID_PASSWORD_LENGTH)
    private String password;

}
