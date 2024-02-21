package com.intuit.demo.service;

import com.intuit.demo.enumeration.DriverBackgroundVerificationStatus;
import com.intuit.demo.model.request.DriverBackgroundVerificationRequestModel;
import com.intuit.demo.model.response.DriverBackgroundVerificationResponseModel;

public interface DriverBackgroundVerificationService {

    DriverBackgroundVerificationResponseModel getDriverBackgroundVerificationDetails(Long userId);

    DriverBackgroundVerificationResponseModel updateDriverBackgroundVerificationDetails(
            DriverBackgroundVerificationRequestModel driverBackgroundVerificationRequestModel);

    DriverBackgroundVerificationResponseModel addDriverBackgroundVerificationDetails(
            DriverBackgroundVerificationRequestModel driverBackgroundVerificationRequestModel);
}
