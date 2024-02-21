package com.intuit.demo.controller;

import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.model.request.DriverRequestModel;
import com.intuit.demo.model.request.DriverStatusRequestModel;
import com.intuit.demo.model.response.APIResponse;
import com.intuit.demo.model.response.DriverResponseModel;
import com.intuit.demo.model.response.DriverStatusResponseModel;
import com.intuit.demo.service.AdminService;
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
@RequestMapping("/v1/admin")
public class AdminController extends BaseRestController {

    @Autowired
    private AdminService adminService;

    @PatchMapping (path ="/driver/status", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public APIResponse<DriverStatusResponseModel> setDriverStatus(@RequestHeader MultiValueMap<String, String> headers,
                                                           @Valid @RequestBody DriverStatusRequestModel driverStatusRequestModel) {
        DriverStatusResponseModel driverStatusResponseModel = adminService.changeDriverStatus(driverStatusRequestModel);
        return APIResponseUtil.createSuccessAPIResponse(driverStatusResponseModel);
    }

    @GetMapping (path ="/driver/status/{userId}")
    public APIResponse<DriverStatusResponseModel> getDriverStatus(@RequestHeader MultiValueMap<String, String> headers,
                                                                   @PathVariable Long userId) {
        DriverStatusResponseModel driverStatusResponseModel = adminService.getDriverStatus(userId);
        return APIResponseUtil.createSuccessAPIResponse(driverStatusResponseModel);
    }

}
