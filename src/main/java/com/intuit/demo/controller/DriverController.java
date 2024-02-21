package com.intuit.demo.controller;

import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.model.request.DriverRequestModel;
import com.intuit.demo.model.request.DriverRideStatusRequestModel;
import com.intuit.demo.model.response.APIResponse;
import com.intuit.demo.model.response.DriverResponseModel;
import com.intuit.demo.model.response.DriverRideStatusResponseModel;
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
@RequestMapping("/v1/driver")
public class DriverController extends BaseRestController {

    @Autowired
    private DriverService driverService;

    @GetMapping("/")
    public APIResponse<DriverResponseModel> getDriverDetails(@RequestHeader MultiValueMap<String, String> headers) {
        Long userId = Long.valueOf(headers.getFirst(AppConstant.X_USER_ID));
        log.info("Getting details for "+userId);
        DriverResponseModel driverResponseModel = driverService.getById(userId);
        return APIResponseUtil.createSuccessAPIResponse(driverResponseModel);
    }

    @PostMapping (path ="/onboard-details", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public APIResponse<DriverResponseModel> addDriverDetails(@RequestHeader MultiValueMap<String, String> headers,
                                                             @Valid @RequestBody DriverRequestModel driverRequestModel) {
        Long userId = Long.valueOf(headers.getFirst(AppConstant.X_USER_ID));
        DriverResponseModel driverResponseModel = driverService.addDriverDetails(userId, driverRequestModel);
        return APIResponseUtil.createSuccessAPIResponse(driverResponseModel);
    }

    @PostMapping (path ="/ride-status", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public APIResponse<DriverRideStatusResponseModel> addDriverRideStatusDetails(@RequestHeader MultiValueMap<String, String> headers,
                                               @Valid @RequestBody DriverRideStatusRequestModel driverRideStatusRequestModel) {
        Long userId = Long.valueOf(headers.getFirst(AppConstant.X_USER_ID));
        DriverRideStatusResponseModel driverRideStatusResponseModel = driverService.addDriverRideStatus(userId, driverRideStatusRequestModel);
        return APIResponseUtil.createSuccessAPIResponse(driverRideStatusResponseModel);
    }

    @GetMapping (path ="/ride-status")
    public APIResponse<DriverRideStatusResponseModel> getDriverRideStatusDetails(@RequestHeader MultiValueMap<String, String> headers) {
        Long userId = Long.valueOf(headers.getFirst(AppConstant.X_USER_ID));
        DriverRideStatusResponseModel driverRideStatusResponseModel = driverService.getDriverRideStatus(userId);
        return APIResponseUtil.createSuccessAPIResponse(driverRideStatusResponseModel);
    }

//    @PatchMapping (path ="/update-details", consumes = {MediaType.APPLICATION_JSON_VALUE})
//    public APIResponse<DriverResponseModel> updateDriverDetails(@RequestHeader MultiValueMap<String, String> headers,
//                                                                @RequestBody DriverRequestModel driverRequestModel) {
//        Long userId = Long.valueOf(headers.getFirst(AppConstant.X_USER_ID));
//        DriverResponseModel driverResponseModel = driverService.updateDriverDetails(userId, driverRequestModel);
//        return APIResponseUtil.createSuccessAPIResponse(driverResponseModel);
//    }
}
