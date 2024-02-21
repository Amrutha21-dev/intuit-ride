package com.intuit.demo.model.response;

import com.intuit.demo.enumeration.DriverStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DriverStatusResponseModel extends ResponseBaseModel{

    private DriverStatus status;
}
