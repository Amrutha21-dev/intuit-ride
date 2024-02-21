package com.intuit.demo.model.response;

import com.intuit.demo.model.response.ResponseBaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponseModel extends ResponseBaseModel {

    private String jwtToken;

    private String refreshToken;

    private Long userId;

}
