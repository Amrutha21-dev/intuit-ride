package com.intuit.demo.service.impl;

import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.entity.DriverBackgroundVerificationEntity;
import com.intuit.demo.entity.DriverEntity;
import com.intuit.demo.entity.DriverRideStatusEntity;
import com.intuit.demo.enumeration.DriverBackgroundVerificationStatus;
import com.intuit.demo.enumeration.DriverRideStatus;
import com.intuit.demo.enumeration.DriverStatus;
import com.intuit.demo.exception.ValidationFailureException;
import com.intuit.demo.model.request.DriverRequestModel;
import com.intuit.demo.model.request.DriverRideStatusRequestModel;
import com.intuit.demo.model.request.UserRequestModel;
import com.intuit.demo.model.response.DriverResponseModel;
import com.intuit.demo.model.response.DriverRideStatusResponseModel;
import com.intuit.demo.model.response.Error;
import com.intuit.demo.repository.DriverBackgroundVerificationRepository;
import com.intuit.demo.repository.DriverRepository;
import com.intuit.demo.repository.DriverRideStatusRepository;
import com.intuit.demo.repository.DriverVehicleRepository;
import com.intuit.demo.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverRideStatusRepository driverRideStatusRepository;

    @Autowired
    private DriverBackgroundVerificationRepository driverBackgroundVerificationRepository;

    @Autowired
    private DriverVehicleRepository driverVehicleRepository;

    @Override
    @Transactional
    public DriverResponseModel getById(Long id) {
        List<Error> errorList = new ArrayList<>();
        Optional<DriverEntity> driverEntityOptional = driverRepository.findById(id);
        if(driverEntityOptional.isEmpty()){
            errorList.add(new Error(ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_NOT_VALID));
        }
        else{
            DriverEntity driverEntity = driverEntityOptional.get();
            return buildDriverResponseFromEntity(driverEntity);
        }
        throw new ValidationFailureException(errorList);
    }

    @Override
    public void addDriver(UserRequestModel userRequestModel, Long userId) {
        DriverEntity driverEntity = DriverEntity.builder().
                id(userId).
                driverStatus(DriverStatus.NEW).
                firstName(userRequestModel.getFirstName()).
                lastName(userRequestModel.getLastName()).
                build();
        driverEntity = driverRepository.save(driverEntity);
        DriverRideStatusEntity driverRideStatusEntity = new DriverRideStatusEntity();
        driverRideStatusEntity.setId(driverEntity.getId());
        driverRideStatusEntity.setDriverRideStatus(DriverRideStatus.NOT_READY);
        driverRideStatusRepository.save(driverRideStatusEntity);
    }

    @Override
    public DriverResponseModel addDriverDetails(Long id, DriverRequestModel driverRequestModel) {
        List<Error> errorList = new ArrayList<>();
        Optional<DriverEntity> driverEntityOptional = driverRepository.findById(id);
        if(driverEntityOptional.isEmpty()){
            errorList.add(new Error(ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_NOT_VALID));
        }
        else{
            DriverEntity driverEntity = driverEntityOptional.get();
            buildDriverEntityFromRequest(driverEntity,driverRequestModel);
            driverEntity.setDriverStatus(DriverStatus.ACTIVE);
            driverEntity = driverRepository.save(driverEntity);
            //todo send message to kafka
            return buildDriverResponseFromEntity(driverEntity);
        }
        throw new ValidationFailureException(errorList);
    }

    @Override
    public DriverResponseModel updateDriverDetails(Long id, DriverRequestModel driverRequestModel) {
        List<Error> errorList = new ArrayList<>();
        Optional<DriverEntity> driverEntityOptional = driverRepository.findById(id);
        if(driverEntityOptional.isEmpty()){
            errorList.add(new Error(ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_NOT_VALID));
        }
        else{
            DriverEntity driverEntity = driverEntityOptional.get();
            buildDriverEntityFromRequest(driverEntity,driverRequestModel);
            driverEntity = driverRepository.save(driverEntity);
            return buildDriverResponseFromEntity(driverEntity);
        }
        throw new ValidationFailureException(errorList);
    }

    @Override
    public DriverRideStatusResponseModel addDriverRideStatus(Long userId, DriverRideStatusRequestModel driverRideStatusRequestModel) {
        List<Error> errorList = new ArrayList<>();
        Optional<DriverRideStatusEntity> driverRideStatusEntityOptional = driverRideStatusRepository.findById(userId);
        if(driverRideStatusEntityOptional.isEmpty()){
            errorList.add(new Error(ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_NOT_VALID));
        }
        else {
            Optional<DriverBackgroundVerificationEntity> driverBackgroundVerificationEntityOptional =
                    driverBackgroundVerificationRepository.findById(userId);
            if (DriverRideStatus.READY.equals(driverRideStatusRequestModel.getStatus()) &&
                    ((driverBackgroundVerificationEntityOptional.isEmpty() ||
                    !DriverStatus.ACTIVE.equals(driverBackgroundVerificationEntityOptional.get().
                            getDriverEntity().getDriverStatus()) ||
                    !DriverBackgroundVerificationStatus.VERIFICATION_SUCCESSFUL.equals
                            (driverBackgroundVerificationEntityOptional.get().getDriverBackgroundVerificationStatus())) ||
                    driverVehicleRepository.findById(userId).isEmpty())) {
                errorList.add(new Error(ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_NOT_VALID));
            } else {
                DriverRideStatusEntity driverRideStatusEntity = driverRideStatusEntityOptional.get();
                driverRideStatusEntity.setDriverRideStatus(driverRideStatusRequestModel.getStatus());
                driverRideStatusEntity = driverRideStatusRepository.save(driverRideStatusEntity);
                return DriverRideStatusResponseModel.builder().
                        status(driverRideStatusEntity.
                                getDriverRideStatus()).build();
            }
        }
        throw new ValidationFailureException(errorList);
    }

    @Override
    public DriverRideStatusResponseModel getDriverRideStatus(Long userId) {
        List<Error> errorList = new ArrayList<>();
        Optional<DriverRideStatusEntity> driverRideStatusEntityOptional = driverRideStatusRepository.findById(userId);
        if(driverRideStatusEntityOptional.isEmpty()){
            errorList.add(new Error(ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_NOT_VALID));
        }
        else{
            return DriverRideStatusResponseModel.builder().
                    status(driverRideStatusEntityOptional.get().
                            getDriverRideStatus()).build();
        }
        throw new ValidationFailureException(errorList);
    }

    private void buildDriverEntityFromRequest(DriverEntity driverEntity, DriverRequestModel driverRequestModel){
        if(driverRequestModel.getFirstName() != null) driverEntity.setFirstName(driverRequestModel.getFirstName());
        if(driverRequestModel.getLastName() != null) driverEntity.setLastName(driverRequestModel.getLastName());
        if(driverRequestModel.getAdhaarNumber() != null) driverEntity.setAdhaarNumber(driverRequestModel.getAdhaarNumber());
        if(driverRequestModel.getPhoneNumber() != null) driverEntity.setPhoneNumber(driverRequestModel.getPhoneNumber());
        if(driverRequestModel.getDrivingLicenseNumber() != null) driverEntity.setDrivingLicenseNumber(driverRequestModel.getDrivingLicenseNumber());
        if(driverRequestModel.getFormattedAddress() != null) driverEntity.setFormattedAddress(driverRequestModel.getFormattedAddress());
        if(driverRequestModel.getCity() != null) driverEntity.setCity(driverRequestModel.getCity());
        if(driverRequestModel.getCountry() != null) driverEntity.setCountry(driverRequestModel.getCountry());
        if(driverRequestModel.getPincode() != null) driverEntity.setPincode(driverRequestModel.getPincode());
        if(driverRequestModel.getState() != null) driverEntity.setState(driverRequestModel.getState());
    }

    private DriverResponseModel buildDriverResponseFromEntity(DriverEntity driverEntity){
        return DriverResponseModel.builder().
                firstName(driverEntity.getFirstName()).
                lastName(driverEntity.getLastName()).
                adhaarNumber(driverEntity.getAdhaarNumber()).
                phoneNumber(driverEntity.getPhoneNumber()).
                drivingLicenseNumber(driverEntity.getDrivingLicenseNumber()).
                formattedAddress(driverEntity.getFormattedAddress()).
                city(driverEntity.getCity()).
                country(driverEntity.getCountry()).
                pincode(driverEntity.getPincode()).
                state(driverEntity.getState()).
                build();
    }
}
