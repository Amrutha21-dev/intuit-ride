package com.intuit.demo.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DriverResponseModel extends ResponseBaseModel{

    private String firstName;
    private String lastName;
    private String adhaarNumber;
    private String drivingLicenseNumber;
    private String phoneNumber;
    private String formattedAddress;
    private String city;
    private String country;
    private String pincode;
    private String state;
}
