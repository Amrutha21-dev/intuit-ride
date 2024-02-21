package com.intuit.demo.service;

import com.intuit.demo.model.request.DriverStatusRequestModel;
import com.intuit.demo.model.response.DriverStatusResponseModel;

public interface AdminService {

    DriverStatusResponseModel changeDriverStatus(DriverStatusRequestModel driverStatusRequestModel);

    DriverStatusResponseModel getDriverStatus(Long userId);
}
