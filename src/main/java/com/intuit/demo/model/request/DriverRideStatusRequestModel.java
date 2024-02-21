package com.intuit.demo.model.request;

import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.enumeration.DriverRideStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverRideStatusRequestModel extends RequestBaseModel {

    @NotNull(message = ErrorConstant.STATUS_EMPTY)
    private DriverRideStatus status;
}
