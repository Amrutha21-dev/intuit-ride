package com.intuit.demo.model.request;

import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.enumeration.VehicleType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DriverVehicleRequestModel extends RequestBaseModel{

    @NotNull(message = ErrorConstant.VEHICLE_TYPE_EMPTY)
    private VehicleType vehicleType;

    @NotNull(message = ErrorConstant.VEHICLE_NUMBER_EMPTY)
    private String vehicleNumber;

    @NotNull(message = ErrorConstant.VEHICLE_CHASSIS_EMPTY)
    private String chassisNumber;

    @NotNull(message = ErrorConstant.VEHICLE_MODEL_EMPTY)
    private String model;
}
