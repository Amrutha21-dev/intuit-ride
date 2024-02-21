package com.intuit.demo.service;

import com.intuit.demo.model.request.DriverVehicleRequestModel;
import com.intuit.demo.model.response.DriverVehicleResponseModel;

public interface DriverVehicleService {
    DriverVehicleResponseModel getById(Long userId);

    DriverVehicleResponseModel addDriverDetails(Long userId, DriverVehicleRequestModel driverVehicleRequestModel);
}
