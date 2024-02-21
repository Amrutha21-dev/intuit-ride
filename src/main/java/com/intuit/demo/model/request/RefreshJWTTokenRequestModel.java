package com.intuit.demo.model.request;

import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.model.request.RequestBaseModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshJWTTokenRequestModel extends RequestBaseModel {

    @NotBlank(message = ErrorConstant.REFRESH_TOKEN_EMPTY)
    private String refreshToken;
}
