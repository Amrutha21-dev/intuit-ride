package com.intuit.demo.service.impl;

import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.entity.DriverVehicleEntity;
import com.intuit.demo.exception.ValidationFailureException;
import com.intuit.demo.model.request.DriverVehicleRequestModel;
import com.intuit.demo.model.response.DriverVehicleResponseModel;
import com.intuit.demo.model.response.Error;
import com.intuit.demo.repository.DriverVehicleRepository;
import com.intuit.demo.service.DriverVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DriverVehicleServiceImpl implements DriverVehicleService {

    @Autowired
    private DriverVehicleRepository driverVehicleRepository;
    @Override
    public DriverVehicleResponseModel getById(Long userId) {
        List<Error> errorList = new ArrayList<>();
        Optional<DriverVehicleEntity> driverVehicleEntityOptional =
                driverVehicleRepository.findById(userId);
        if(driverVehicleEntityOptional.isEmpty()){
            errorList.add(new Error(ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_NOT_VALID));
        }
        else{
            return buildDriverVehicleResponseModel(driverVehicleEntityOptional.get());
        }
        throw new ValidationFailureException(errorList);
    }

    @Override
    public DriverVehicleResponseModel addDriverDetails(Long userId, DriverVehicleRequestModel driverVehicleRequestModel) {
        DriverVehicleEntity driverVehicleEntity = buildDriverVehicleEntity(driverVehicleRequestModel);
        driverVehicleEntity.setId(userId);
        driverVehicleEntity = driverVehicleRepository.save(driverVehicleEntity);
        return buildDriverVehicleResponseModel(driverVehicleEntity);
    }

    private DriverVehicleResponseModel buildDriverVehicleResponseModel(DriverVehicleEntity driverVehicleEntity){
        return DriverVehicleResponseModel.builder().
                vehicleNumber(driverVehicleEntity.getVehicleNumber()).
                chassisNumber(driverVehicleEntity.getChassisNumber()).
                vehicleType(driverVehicleEntity.getVehicleType()).
                model(driverVehicleEntity.getModel()).
                build();
    }

    private DriverVehicleEntity buildDriverVehicleEntity(DriverVehicleRequestModel driverVehicleRequestModel){
        return DriverVehicleEntity.builder().
                vehicleNumber(driverVehicleRequestModel.getVehicleNumber()).
                chassisNumber(driverVehicleRequestModel.getChassisNumber()).
                vehicleType(driverVehicleRequestModel.getVehicleType()).
                model(driverVehicleRequestModel.getModel()).
                build();
    }
}
