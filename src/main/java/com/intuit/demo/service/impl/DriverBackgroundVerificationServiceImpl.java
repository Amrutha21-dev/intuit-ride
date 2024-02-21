package com.intuit.demo.service.impl;

import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.entity.DriverBackgroundVerificationEntity;
import com.intuit.demo.exception.ValidationFailureException;
import com.intuit.demo.model.request.DriverBackgroundVerificationRequestModel;
import com.intuit.demo.model.response.DriverBackgroundVerificationResponseModel;
import com.intuit.demo.model.response.Error;
import com.intuit.demo.repository.DriverBackgroundVerificationRepository;
import com.intuit.demo.service.DriverBackgroundVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DriverBackgroundVerificationServiceImpl implements DriverBackgroundVerificationService {

    @Autowired
    private DriverBackgroundVerificationRepository driverBackgroundVerificationRepository;

    @Override
    public DriverBackgroundVerificationResponseModel getDriverBackgroundVerificationDetails(Long userId) {
        List<Error> errorList = new ArrayList<>();
        Optional<DriverBackgroundVerificationEntity> driverBackgroundVerificationEntityOptional =
                driverBackgroundVerificationRepository.findById(userId);
        if(driverBackgroundVerificationEntityOptional.isEmpty()){
            errorList.add(new Error(ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_NOT_VALID));
        }
        else{
            return DriverBackgroundVerificationResponseModel.builder().
                    status(driverBackgroundVerificationEntityOptional.get().
                            getDriverBackgroundVerificationStatus()).build();
        }
        throw new ValidationFailureException(errorList);
    }

    @Override
    public DriverBackgroundVerificationResponseModel updateDriverBackgroundVerificationDetails(
            DriverBackgroundVerificationRequestModel driverBackgroundVerificationRequestModel) {
        List<Error> errorList = new ArrayList<>();
        DriverBackgroundVerificationEntity driverBackgroundVerificationEntity = driverBackgroundVerificationRepository.
                findByDriverEntity_AdhaarNumber(driverBackgroundVerificationRequestModel.getAadhaarNumber());
        if(driverBackgroundVerificationEntity == null){
            errorList.add(new Error(ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_NOT_VALID));
        }
        else{
            driverBackgroundVerificationEntity.setDriverBackgroundVerificationStatus(driverBackgroundVerificationRequestModel.getStatus());
            driverBackgroundVerificationEntity = driverBackgroundVerificationRepository.save(driverBackgroundVerificationEntity);
            return DriverBackgroundVerificationResponseModel.builder().
                    status(driverBackgroundVerificationEntity.getDriverBackgroundVerificationStatus()).build();
        }
        throw new ValidationFailureException(errorList);
    }

    @Override
    public DriverBackgroundVerificationResponseModel addDriverBackgroundVerificationDetails(
            DriverBackgroundVerificationRequestModel driverBackgroundVerificationRequestModel) {
        DriverBackgroundVerificationEntity driverBackgroundVerificationEntity = new DriverBackgroundVerificationEntity();
        driverBackgroundVerificationEntity.setDriverBackgroundVerificationStatus(driverBackgroundVerificationRequestModel.getStatus());
        driverBackgroundVerificationEntity.setId(driverBackgroundVerificationRequestModel.getId());
        driverBackgroundVerificationEntity = driverBackgroundVerificationRepository.save(driverBackgroundVerificationEntity);
        return DriverBackgroundVerificationResponseModel.builder().
                status(driverBackgroundVerificationEntity.getDriverBackgroundVerificationStatus()).build();
    }
}
