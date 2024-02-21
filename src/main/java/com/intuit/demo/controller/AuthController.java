package com.intuit.demo.controller;

import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.model.request.*;
import com.intuit.demo.model.response.APIResponse;
import com.intuit.demo.model.response.EmptyResponse;
import com.intuit.demo.model.response.LoginResponseModel;
import com.intuit.demo.service.AuthService;
import com.intuit.demo.service.DriverService;
import com.intuit.demo.util.APIResponseUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1")
public class AuthController extends BaseRestController {

    @Autowired
    private AuthService authService;

    @Autowired
    private DriverService driverService;

    @PostMapping ("/driver/register")
    public APIResponse<LoginResponseModel> registerDriver(@RequestHeader MultiValueMap<String, String> headers,
                                                          @Valid @RequestBody UserRequestModel userRequestModel) {
        log.info("Creating the driver");
        LoginResponseModel loginResponseModel = authService.registerDriver(userRequestModel,
                headers.getFirst(AppConstant.X_SOURCE_IP_ADDRESS));
        driverService.addDriver(userRequestModel, loginResponseModel.getUserId());
        return APIResponseUtil.createSuccessAPIResponse(loginResponseModel);
    }

    @PostMapping ("/admin/register")
    public APIResponse<LoginResponseModel> registerAdmin(@RequestHeader MultiValueMap<String, String> headers,
                                                          @Valid @RequestBody UserRequestModel userRequestModel) {
        log.info("Creating the admin");
        LoginResponseModel loginResponseModel = authService.registerAdmin(userRequestModel,
                headers.getFirst(AppConstant.X_SOURCE_IP_ADDRESS));
        return APIResponseUtil.createSuccessAPIResponse(loginResponseModel);
    }

    @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public APIResponse<LoginResponseModel> authenticateLogin(@RequestHeader MultiValueMap<String, String> headers,
                                                        @Valid @RequestBody LoginRequestModel loginRequestModel) {
        LoginResponseModel loginResponseModel = authService.login(loginRequestModel,
                headers.getFirst(AppConstant.X_SOURCE_IP_ADDRESS));
        return APIResponseUtil.createSuccessAPIResponse(loginResponseModel);
    }

    @PostMapping(path = "/refresh-jwt-token", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public APIResponse<LoginResponseModel> refreshJwtToken(@RequestHeader MultiValueMap<String, String> headers,
                                          @Valid @RequestBody RefreshJWTTokenRequestModel refreshJWTTokenRequestModel) {
        log.info("Refreshing Jwt Token");
        Long userId = Long.valueOf(headers.getFirst(AppConstant.X_USER_ID));
        LoginResponseModel loginResponse = authService.refreshJwtToken(userId, refreshJWTTokenRequestModel,
                headers.getFirst(AppConstant.X_SOURCE_IP_ADDRESS));
        return APIResponseUtil.createSuccessAPIResponse(loginResponse);
    }

    @PutMapping(path = "/set-new-password", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public APIResponse<EmptyResponse> setNewPassword(@RequestHeader MultiValueMap<String, String> headers,
                                                     @Valid @RequestBody NewPasswordRequestModel newPasswordRequestModel) {
        log.info("Setting new password");
        EmptyResponse emptyResponse = authService.setNewPassword(newPasswordRequestModel);
        return APIResponseUtil.createSuccessAPIResponse(emptyResponse);
    }

    @PostMapping(path = "/forgot-password", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public APIResponse<EmptyResponse> sendResetPasswordToken(@RequestHeader MultiValueMap<String, String> headers,
                                             @Valid @RequestBody ResetPasswordRequestModel resetPasswordRequestModel) {
        log.info("Sending email with reset link");
        EmptyResponse emptyResponse = authService.sendResetPasswordToken(resetPasswordRequestModel);
        return APIResponseUtil.createSuccessAPIResponse(emptyResponse);
    }

    @PostMapping(path = "/logout", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public APIResponse<EmptyResponse> logout(@RequestHeader MultiValueMap<String, String> headers) {
        log.info("Logging out the user");
        Long userId = Long.valueOf(headers.getFirst(AppConstant.X_USER_ID));
        EmptyResponse emptyResponse = authService.logout(userId);
        return APIResponseUtil.createSuccessAPIResponse(emptyResponse);
    }
}
