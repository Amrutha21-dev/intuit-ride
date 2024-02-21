package com.intuit.demo.model.response;

import com.intuit.demo.enumeration.VehicleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DriverVehicleResponseModel extends ResponseBaseModel{

    private VehicleType vehicleType;

    private String vehicleNumber;

    private String chassisNumber;

    private String model;

}
