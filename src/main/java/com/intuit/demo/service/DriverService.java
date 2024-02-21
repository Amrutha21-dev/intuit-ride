package com.intuit.demo.service;

import com.intuit.demo.model.request.DriverRequestModel;
import com.intuit.demo.model.request.DriverRideStatusRequestModel;
import com.intuit.demo.model.request.UserRequestModel;
import com.intuit.demo.model.response.DriverResponseModel;
import com.intuit.demo.model.response.DriverRideStatusResponseModel;

public interface DriverService {

    DriverResponseModel getById(Long id);

    void addDriver(UserRequestModel model,Long userId);

    DriverResponseModel addDriverDetails(Long id, DriverRequestModel driverRequestModel);

    DriverResponseModel updateDriverDetails(Long id, DriverRequestModel driverRequestModel);

    DriverRideStatusResponseModel addDriverRideStatus(Long userId, DriverRideStatusRequestModel driverRequestModel);

    DriverRideStatusResponseModel getDriverRideStatus(Long userId);
}
