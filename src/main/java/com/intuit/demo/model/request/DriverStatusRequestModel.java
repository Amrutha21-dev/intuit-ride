package com.intuit.demo.model.request;

import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.enumeration.DriverStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverStatusRequestModel extends RequestBaseModel {

    @NotNull(message = ErrorConstant.IDENTITIER_NOT_VALID)
    private Long userId;

    @NotNull(message = ErrorConstant.STATUS_EMPTY)
    private DriverStatus status;
}
