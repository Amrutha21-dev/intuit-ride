package com.intuit.demo.model.response;

import com.intuit.demo.enumeration.DriverBackgroundVerificationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DriverBackgroundVerificationResponseModel extends ResponseBaseModel{

    private DriverBackgroundVerificationStatus status;
}
