package com.intuit.demo.controller;

import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.model.request.DriverVehicleRequestModel;
import com.intuit.demo.model.response.APIResponse;
import com.intuit.demo.model.response.DriverVehicleResponseModel;
import com.intuit.demo.service.DriverVehicleService;
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
public class VehicleController extends BaseRestController {

    @Autowired
    private DriverVehicleService driverVehicleService;

    @GetMapping("/vehicle-details")
    public APIResponse<DriverVehicleResponseModel> getDriverDetails(@RequestHeader MultiValueMap<String, String> headers) {
        Long userId = Long.valueOf(headers.getFirst(AppConstant.X_USER_ID));
        log.info("Getting details for "+userId);
        DriverVehicleResponseModel driverVehicleResponseModel = driverVehicleService.getById(userId);
        return APIResponseUtil.createSuccessAPIResponse(driverVehicleResponseModel);
    }

    @PostMapping (path ="/vehicle-details", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public APIResponse<DriverVehicleResponseModel> addDriverDetails(@RequestHeader MultiValueMap<String, String> headers,
                                                    @Valid @RequestBody DriverVehicleRequestModel driverVehicleRequestModel) {
        Long userId = Long.valueOf(headers.getFirst(AppConstant.X_USER_ID));
        DriverVehicleResponseModel driverVehicleResponseModel = driverVehicleService.addDriverDetails(userId, driverVehicleRequestModel);
        return APIResponseUtil.createSuccessAPIResponse(driverVehicleResponseModel);
    }
}
