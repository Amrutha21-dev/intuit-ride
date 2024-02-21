package com.intuit.demo.model.request;

import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.constant.ErrorConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DriverRequestModel extends RequestBaseModel{

    private String firstName;

    private String lastName;

    @Pattern(regexp = AppConstant.ADHAAR_NUMBER_REGEX, message = ErrorConstant.ADHAAR_NUMBER_NOT_VALID)
    private String adhaarNumber;

    @Pattern(regexp = AppConstant.PHONE_NUMBER_REGEX, message = ErrorConstant.PHONE_NUMBER_NOT_VALID)
    private String phoneNumber;

    @NotBlank(message = ErrorConstant.DRIVING_LICENSE_EMPTY)
    private String drivingLicenseNumber;

    @NotBlank(message = ErrorConstant.FORMATTED_ADDRESS_EMPTY)
    private String formattedAddress;

    @NotBlank(message = ErrorConstant.CITY_EMPTY)
    private String city;

    @NotBlank(message = ErrorConstant.COUNTRY_EMPTY)
    private String country;

    @NotBlank(message = ErrorConstant.PINCODE_EMPTY)
    private String pincode;

    @NotBlank(message = ErrorConstant.STATE_EMPTY)
    private String state;
}
