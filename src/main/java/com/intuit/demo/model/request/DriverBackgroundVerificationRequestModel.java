package com.intuit.demo.model.request;

import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.enumeration.DriverBackgroundVerificationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DriverBackgroundVerificationRequestModel extends RequestBaseModel {

    @Pattern(regexp = AppConstant.ADHAAR_NUMBER_REGEX, message = ErrorConstant.ADHAAR_NUMBER_NOT_VALID)
    private String aadhaarNumber;

    @NotNull(message = ErrorConstant.STATUS_EMPTY)
    private DriverBackgroundVerificationStatus status;

    //only for dev purpose without kafka queue
    private Long id;
}
