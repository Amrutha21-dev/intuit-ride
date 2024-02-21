package com.intuit.demo.controller;

import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.model.request.DriverBackgroundVerificationRequestModel;
import com.intuit.demo.model.response.APIResponse;
import com.intuit.demo.model.response.DriverBackgroundVerificationResponseModel;
import com.intuit.demo.service.DriverBackgroundVerificationService;
import com.intuit.demo.util.APIResponseUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/driver/background-verification/")
public class DriverBackgroundVerificationController extends BaseRestController {

    @Autowired
    private DriverBackgroundVerificationService driverBackgroundVerificationService;

    @GetMapping("/status")
    public APIResponse<DriverBackgroundVerificationResponseModel> getDriverBackgroundVerificationDetails
            (@RequestHeader MultiValueMap<String, String> headers) {
        Long userId = Long.valueOf(headers.getFirst(AppConstant.X_USER_ID));
        log.info("Getting details for background verification of driver "+userId);
        DriverBackgroundVerificationResponseModel driverBackgroundVerificationResponseModel =
                driverBackgroundVerificationService.getDriverBackgroundVerificationDetails
                        (userId);
        return APIResponseUtil.createSuccessAPIResponse(driverBackgroundVerificationResponseModel);
    }

    @PutMapping (path ="/external/status", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public APIResponse<DriverBackgroundVerificationResponseModel> updateBackgroundVerificationDriverDetails
            (@RequestHeader MultiValueMap<String, String> headers,
                           @Valid @RequestBody DriverBackgroundVerificationRequestModel driverBackgroundVerificationRequestModel) {
        log.info("Updating details for background verification of driver");
        DriverBackgroundVerificationResponseModel driverBackgroundVerificationResponseModel =
                driverBackgroundVerificationService.updateDriverBackgroundVerificationDetails
                        (driverBackgroundVerificationRequestModel);
        return APIResponseUtil.createSuccessAPIResponse(driverBackgroundVerificationResponseModel);
    }

    @PostMapping (path ="/external/status", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public APIResponse<DriverBackgroundVerificationResponseModel> addDriverBackgroundVerificationDetails
            (@RequestHeader MultiValueMap<String, String> headers,
               @RequestBody DriverBackgroundVerificationRequestModel driverBackgroundVerificationRequestModel) {
        log.info("Updating details for background verification of driver");
        DriverBackgroundVerificationResponseModel driverBackgroundVerificationResponseModel =
                driverBackgroundVerificationService.addDriverBackgroundVerificationDetails
                        (driverBackgroundVerificationRequestModel);
        return APIResponseUtil.createSuccessAPIResponse(driverBackgroundVerificationResponseModel);
    }
}
