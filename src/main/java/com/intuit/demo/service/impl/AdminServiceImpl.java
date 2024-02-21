package com.intuit.demo.service.impl;

import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.entity.DriverEntity;
import com.intuit.demo.entity.DriverRideStatusEntity;
import com.intuit.demo.enumeration.DriverRideStatus;
import com.intuit.demo.exception.ValidationFailureException;
import com.intuit.demo.model.request.DriverStatusRequestModel;
import com.intuit.demo.model.response.DriverStatusResponseModel;
import com.intuit.demo.model.response.Error;
import com.intuit.demo.repository.DriverRepository;
import com.intuit.demo.repository.DriverRideStatusRepository;
import com.intuit.demo.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverRideStatusRepository driverRideStatusRepository;

    @Override
    public DriverStatusResponseModel changeDriverStatus(DriverStatusRequestModel driverStatusRequestModel) {
        List<Error> errorList = new ArrayList<>();
        Optional<DriverEntity> driverEntityOptional = driverRepository.findById(driverStatusRequestModel.getUserId());
        if(driverEntityOptional.isEmpty()){
            errorList.add(new Error(ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_NOT_VALID));
        }
        else{
            DriverEntity driverEntity = driverEntityOptional.get();
            driverEntity.setDriverStatus(driverStatusRequestModel.getStatus());
            driverEntity = driverRepository.save(driverEntity);
            Optional<DriverRideStatusEntity> driverRideStatusEntityOptional =
                driverRideStatusRepository.findById(driverEntity.getId());
            if(driverRideStatusEntityOptional.isPresent()){
                DriverRideStatusEntity driverRideStatusEntity = driverRideStatusEntityOptional.get();
                driverRideStatusEntity.setDriverRideStatus(DriverRideStatus.NOT_READY);
                driverRideStatusRepository.save(driverRideStatusEntity);
            }
            return DriverStatusResponseModel.builder().
                    status(driverEntity.
                            getDriverStatus()).build();
        }
        throw new ValidationFailureException(errorList);
    }

    @Override
    public DriverStatusResponseModel getDriverStatus(Long userId) {
        List<Error> errorList = new ArrayList<>();
        Optional<DriverEntity> driverEntityOptional = driverRepository.findById(userId);
        if(driverEntityOptional.isEmpty()){
            errorList.add(new Error(ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_NOT_VALID));
        }
        else{
            return DriverStatusResponseModel.builder().
                    status(driverEntityOptional.get().
                            getDriverStatus()).build();
        }
        throw new ValidationFailureException(errorList);
    }
}
