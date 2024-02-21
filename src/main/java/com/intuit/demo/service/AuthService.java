package com.intuit.demo.service;

import com.intuit.demo.model.request.*;
import com.intuit.demo.model.response.LoginResponseModel;
import com.intuit.demo.model.response.EmptyResponse;
import com.intuit.demo.model.request.*;
import org.springframework.util.MultiValueMap;

public interface AuthService {

    LoginResponseModel registerDriver(UserRequestModel userRequestModel, String ip);

    LoginResponseModel registerAdmin(UserRequestModel userRequestModel, String ip);

    LoginResponseModel login(LoginRequestModel loginRequestModel, String ip);

    LoginResponseModel refreshJwtToken(Long userId, RefreshJWTTokenRequestModel refreshJWTTokenRequestModel, String ip);

    EmptyResponse setNewPassword(NewPasswordRequestModel newPasswordRequestModel);

    EmptyResponse sendResetPasswordToken(ResetPasswordRequestModel resetPasswordRequestModel);

    EmptyResponse logout(Long userId);
}
