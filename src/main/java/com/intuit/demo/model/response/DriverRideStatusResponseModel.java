package com.intuit.demo.model.response;

import com.intuit.demo.enumeration.DriverRideStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DriverRideStatusResponseModel extends ResponseBaseModel{

    private DriverRideStatus status;
}
