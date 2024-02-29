package com.intuit.demo.service.impl;

import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.entity.DriverBackgroundVerificationEntity;
import com.intuit.demo.entity.DriverEntity;
import com.intuit.demo.entity.DriverRideStatusEntity;
import com.intuit.demo.entity.DriverVehicleEntity;
import com.intuit.demo.enumeration.DriverBackgroundVerificationStatus;
import com.intuit.demo.enumeration.DriverRideStatus;
import com.intuit.demo.enumeration.DriverStatus;
import com.intuit.demo.enumeration.VehicleType;
import com.intuit.demo.exception.ValidationFailureException;
import com.intuit.demo.model.request.DriverRequestModel;
import com.intuit.demo.model.request.DriverRideStatusRequestModel;
import com.intuit.demo.model.request.UserRequestModel;
import com.intuit.demo.model.response.DriverResponseModel;
import com.intuit.demo.model.response.DriverRideStatusResponseModel;
import com.intuit.demo.repository.DriverBackgroundVerificationRepository;
import com.intuit.demo.repository.DriverRepository;

import com.intuit.demo.repository.DriverRideStatusRepository;
import com.intuit.demo.repository.DriverVehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplTest {

    @InjectMocks
    private DriverServiceImpl driverServiceImpl = new DriverServiceImpl();

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverRideStatusRepository driverRideStatusRepository;

    @Mock
    private DriverBackgroundVerificationRepository driverBackgroundVerificationRepository;

    @Mock
    private DriverVehicleRepository driverVehicleRepository;


    @Test
    public void getByIdSuccess_p() {
            when(driverRepository.findById(3L)).thenReturn(
                Optional.of(DriverEntity.builder().
                    id(3L).
                    firstName("Test").
                    lastName("Driver").
                    adhaarNumber("754865869584").
                    phoneNumber("8456584565").
                    drivingLicenseNumber("KA2232KNDFD").
                    formattedAddress("test Address").
                    city("Bengaluru").
                    country("India").
                    pincode("756312").
                    state("Karnataka").
                    build()
                )
            );


        DriverResponseModel driverResponseModel = driverServiceImpl.getById(3L);
        assertEquals("firstName Should be Test", "Test", driverResponseModel.getFirstName());
        assertEquals("lastName Should be Driver", "Driver", driverResponseModel.getLastName());
        assertEquals("adhaarNumber Should be 754865869584", "754865869584", driverResponseModel.getAdhaarNumber());
        assertEquals("phoneNumber Should be 8456584565", "8456584565", driverResponseModel.getPhoneNumber());
        assertEquals("drivingLicenseNumber Should be KA2232KNDFD", "KA2232KNDFD", driverResponseModel.getDrivingLicenseNumber());
        assertEquals("formattedAddress Should be test Address", "test Address", driverResponseModel.getFormattedAddress());
        assertEquals("city Should be Bengaluru", "Bengaluru", driverResponseModel.getCity());
        assertEquals("country Should be India", "India", driverResponseModel.getCountry());
        assertEquals("pincode Should be 756312", "756312", driverResponseModel.getPincode());
        assertEquals("state Should be Karnataka", "Karnataka", driverResponseModel.getState());

    }

    @Test
    public void getByIdEmpty_n() {
        when(driverRepository.findById(3L)).thenReturn(
                Optional.empty()
        );
        ValidationFailureException validationFailureException =
                assertThrows(ValidationFailureException.class, () -> driverServiceImpl.getById(3L));
        assertEquals("The field Should be" +ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_FIELD, validationFailureException.getErrors().get(0).getField());
        assertEquals("The message Should be" +ErrorConstant.IDENTITIER_NOT_VALID, ErrorConstant.IDENTITIER_NOT_VALID, validationFailureException.getErrors().get(0).getMessage());

    }

    @Test
    public void addDriverSuccess_p() {
        when(driverRepository.save(any())).thenReturn(
                DriverEntity.builder().
                        id(3L).
                        build()
        );

        when(driverRideStatusRepository.save(any())).thenReturn(
                DriverRideStatusEntity.builder().
                        id(3L).
                        build()
        );
        UserRequestModel userRequestModel = new UserRequestModel();
        userRequestModel.setEmail("test@test.com");
        userRequestModel.setFirstName("test");
        userRequestModel.setLastName("Driver");
        userRequestModel.setPassword("%tdgDk12");


        driverServiceImpl.addDriver(userRequestModel, 3L);

        verify(driverRepository, times(1)).save(any());
        verify(driverRideStatusRepository, times(1)).save(any());

    }

    @Test
    public void addDriverDetailsSuccess_p() {
        DriverEntity driverEntity = DriverEntity.builder().
                id(3L).
                firstName("Test").
                lastName("Driver").
                adhaarNumber("754865869584").
                phoneNumber("8456584565").
                drivingLicenseNumber("KA2232KNDFD").
                formattedAddress("test Address").
                city("Bengaluru").
                country("India").
                pincode("756312").
                state("Karnataka").
                build();
        when(driverRepository.findById(3L)).thenReturn(
                Optional.of(driverEntity)
        );

        when(driverRepository.save(driverEntity)).thenReturn(driverEntity);

        DriverRequestModel driverRequestModel = new DriverRequestModel();
        driverRequestModel.setCity("Mangalore");

        DriverResponseModel driverResponseModel = driverServiceImpl.addDriverDetails(3L, driverRequestModel);

        assertEquals("firstName Should be Test", "Test", driverResponseModel.getFirstName());
        assertEquals("lastName Should be Driver", "Driver", driverResponseModel.getLastName());
        assertEquals("adhaarNumber Should be 754865869584", "754865869584", driverResponseModel.getAdhaarNumber());
        assertEquals("phoneNumber Should be 8456584565", "8456584565", driverResponseModel.getPhoneNumber());
        assertEquals("drivingLicenseNumber Should be KA2232KNDFD", "KA2232KNDFD", driverResponseModel.getDrivingLicenseNumber());
        assertEquals("formattedAddress Should be test Address", "test Address", driverResponseModel.getFormattedAddress());
        assertEquals("city Should be Mangalore", "Mangalore", driverResponseModel.getCity());
        assertEquals("country Should be India", "India", driverResponseModel.getCountry());
        assertEquals("pincode Should be 756312", "756312", driverResponseModel.getPincode());
        assertEquals("state Should be Karnataka", "Karnataka", driverResponseModel.getState());
    }

    @Test
    public void addDriverDetailsEmpty_n() {
        when(driverRepository.findById(3L)).thenReturn(
                Optional.empty()
        );
        DriverRequestModel driverRequestModel = new DriverRequestModel();
        driverRequestModel.setCity("Mangalore");

        ValidationFailureException validationFailureException =
                assertThrows(ValidationFailureException.class, () -> driverServiceImpl.addDriverDetails(3L, driverRequestModel));

        assertEquals("The field Should be" +ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_FIELD, validationFailureException.getErrors().get(0).getField());
        assertEquals("The message Should be" +ErrorConstant.IDENTITIER_NOT_VALID, ErrorConstant.IDENTITIER_NOT_VALID, validationFailureException.getErrors().get(0).getMessage());

    }

    @Test
    public void updateDriverDetailsSuccess_p() {
        DriverEntity driverEntity = DriverEntity.builder().
                id(3L).
                firstName("Test").
                lastName("Driver").
                adhaarNumber("754865869584").
                phoneNumber("8456584565").
                drivingLicenseNumber("KA2232KNDFD").
                formattedAddress("test Address").
                city("Bengaluru").
                country("India").
                pincode("756312").
                state("Karnataka").
                build();
        when(driverRepository.findById(3L)).thenReturn(
                Optional.of(driverEntity)
        );

        when(driverRepository.save(driverEntity)).thenReturn(driverEntity);

        DriverRequestModel driverRequestModel = new DriverRequestModel();
        driverRequestModel.setCity("Mangalore");

        DriverResponseModel driverResponseModel = driverServiceImpl.updateDriverDetails(3L, driverRequestModel);

        assertEquals("firstName Should be Test", "Test", driverResponseModel.getFirstName());
        assertEquals("lastName Should be Driver", "Driver", driverResponseModel.getLastName());
        assertEquals("adhaarNumber Should be 754865869584", "754865869584", driverResponseModel.getAdhaarNumber());
        assertEquals("phoneNumber Should be 8456584565", "8456584565", driverResponseModel.getPhoneNumber());
        assertEquals("drivingLicenseNumber Should be KA2232KNDFD", "KA2232KNDFD", driverResponseModel.getDrivingLicenseNumber());
        assertEquals("formattedAddress Should be test Address", "test Address", driverResponseModel.getFormattedAddress());
        assertEquals("city Should be Mangalore", "Mangalore", driverResponseModel.getCity());
        assertEquals("country Should be India", "India", driverResponseModel.getCountry());
        assertEquals("pincode Should be 756312", "756312", driverResponseModel.getPincode());
        assertEquals("state Should be Karnataka", "Karnataka", driverResponseModel.getState());
    }

    @Test
    public void updateDriverDetailsEmpty_n() {
        when(driverRepository.findById(3L)).thenReturn(
                Optional.empty()
        );
        DriverRequestModel driverRequestModel = new DriverRequestModel();
        driverRequestModel.setCity("Mangalore");

        ValidationFailureException validationFailureException =
                assertThrows(ValidationFailureException.class, () -> driverServiceImpl.updateDriverDetails(3L, driverRequestModel));

        assertEquals("The field Should be" +ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_FIELD, validationFailureException.getErrors().get(0).getField());
        assertEquals("The message Should be" +ErrorConstant.IDENTITIER_NOT_VALID, ErrorConstant.IDENTITIER_NOT_VALID, validationFailureException.getErrors().get(0).getMessage());

    }

    @Test
    public void addDriverRideStatusSuccess_p() {
        DriverEntity driverEntity = DriverEntity.builder().
                id(3L).
                firstName("Test").
                lastName("Driver").
                adhaarNumber("754865869584").
                phoneNumber("8456584565").
                drivingLicenseNumber("KA2232KNDFD").
                formattedAddress("test Address").
                city("Bengaluru").
                country("India").
                pincode("756312").
                state("Karnataka").
                driverStatus(DriverStatus.ACTIVE).
                build();

        DriverRideStatusEntity driverRideStatusEntity = DriverRideStatusEntity.builder().
                driverEntity(driverEntity).
                driverRideStatus(DriverRideStatus.READY).
                id(3L).
                build();
        DriverBackgroundVerificationEntity driverBackgroundVerificationEntity = DriverBackgroundVerificationEntity.builder().
                driverEntity(driverEntity).
                id(3L).
                driverBackgroundVerificationStatus(DriverBackgroundVerificationStatus.VERIFICATION_SUCCESSFUL).
                build();


        when(driverRideStatusRepository.findById(3L)).thenReturn(
                Optional.of(driverRideStatusEntity)
        );
        when(driverRideStatusRepository.save(driverRideStatusEntity)).thenReturn(
                driverRideStatusEntity
        );
        when(driverBackgroundVerificationRepository.findById(3L)).thenReturn(
                Optional.of(driverBackgroundVerificationEntity)
        );
        when(driverVehicleRepository.findById(3L)).thenReturn(
                Optional.of(DriverVehicleEntity.builder().
                        id(3L).
                        model("Ferrari").
                        chassisNumber("DSSSX4323").
                        driverEntity(driverEntity).
                        vehicleNumber("KA01RF5643").
                        vehicleType(VehicleType.COMPACT_CAR).
                        build())
        );

        //when(driverRepository.save(driverEntity)).thenReturn(driverEntity);

        DriverRideStatusRequestModel driverRequestModel = new DriverRideStatusRequestModel();
        driverRequestModel.setStatus(DriverRideStatus.READY);

        DriverRideStatusResponseModel driverRideStatusResponseModel = driverServiceImpl.addDriverRideStatus(3L, driverRequestModel);

        assertEquals("Status Should be Ready", DriverRideStatus.READY, driverRideStatusResponseModel.getStatus());

    }

    @Test
    public void addDriverRideStatusNotActive_n() {
        DriverEntity driverEntity = DriverEntity.builder().
                id(3L).
                firstName("Test").
                lastName("Driver").
                adhaarNumber("754865869584").
                phoneNumber("8456584565").
                drivingLicenseNumber("KA2232KNDFD").
                formattedAddress("test Address").
                city("Bengaluru").
                country("India").
                pincode("756312").
                state("Karnataka").
                driverStatus(DriverStatus.ACTIVE).
                build();

        DriverRideStatusEntity driverRideStatusEntity = DriverRideStatusEntity.builder().
                driverEntity(driverEntity).
                driverRideStatus(DriverRideStatus.READY).
                id(3L).
                build();
        DriverBackgroundVerificationEntity driverBackgroundVerificationEntity = DriverBackgroundVerificationEntity.builder().
                driverEntity(driverEntity).
                id(3L).
                driverBackgroundVerificationStatus(DriverBackgroundVerificationStatus.VERIFICATION_SUCCESSFUL).
                build();


        when(driverRideStatusRepository.findById(3L)).thenReturn(
                Optional.of(driverRideStatusEntity)
        );

        when(driverBackgroundVerificationRepository.findById(3L)).thenReturn(
                Optional.of(driverBackgroundVerificationEntity)
        );
        when(driverVehicleRepository.findById(3L)).thenReturn(
                Optional.empty()
        );


        DriverRideStatusRequestModel driverRequestModel = new DriverRideStatusRequestModel();
        driverRequestModel.setStatus(DriverRideStatus.READY);

        ValidationFailureException validationFailureException =
                assertThrows(ValidationFailureException.class, () -> driverServiceImpl.addDriverRideStatus(3L, driverRequestModel));

        assertEquals("The field Should be" +ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_FIELD, validationFailureException.getErrors().get(0).getField());
        assertEquals("The message Should be" +ErrorConstant.IDENTITIER_NOT_VALID, ErrorConstant.IDENTITIER_NOT_VALID, validationFailureException.getErrors().get(0).getMessage());


    }

    @Test
    public void addDriverRideStatusNotAvailable_n() {


        when(driverRideStatusRepository.findById(3L)).thenReturn(
                Optional.empty()
        );

        DriverRideStatusRequestModel driverRequestModel = new DriverRideStatusRequestModel();
        driverRequestModel.setStatus(DriverRideStatus.READY);

        ValidationFailureException validationFailureException =
                assertThrows(ValidationFailureException.class, () -> driverServiceImpl.addDriverRideStatus(3L, driverRequestModel));

        assertEquals("The field Should be" +ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_FIELD, validationFailureException.getErrors().get(0).getField());
        assertEquals("The message Should be" +ErrorConstant.IDENTITIER_NOT_VALID, ErrorConstant.IDENTITIER_NOT_VALID, validationFailureException.getErrors().get(0).getMessage());


    }

    @Test
    public void getDriverRideStatusSuccess_p() {
        DriverEntity driverEntity = DriverEntity.builder().
                id(3L).
                firstName("Test").
                lastName("Driver").
                adhaarNumber("754865869584").
                phoneNumber("8456584565").
                drivingLicenseNumber("KA2232KNDFD").
                formattedAddress("test Address").
                city("Bengaluru").
                country("India").
                pincode("756312").
                state("Karnataka").
                driverStatus(DriverStatus.ACTIVE).
                build();

        DriverRideStatusEntity driverRideStatusEntity = DriverRideStatusEntity.builder().
                driverEntity(driverEntity).
                driverRideStatus(DriverRideStatus.READY).
                id(3L).
                build();


        when(driverRideStatusRepository.findById(3L)).thenReturn(
                Optional.of(driverRideStatusEntity)
        );

        DriverRideStatusRequestModel driverRequestModel = new DriverRideStatusRequestModel();
        driverRequestModel.setStatus(DriverRideStatus.READY);

        DriverRideStatusResponseModel driverRideStatusResponseModel = driverServiceImpl.getDriverRideStatus(3L);

        assertEquals("Status Should be Ready", DriverRideStatus.READY, driverRideStatusResponseModel.getStatus());

    }

    @Test
    public void getDriverRideStatusEmpty_n() {

        when(driverRideStatusRepository.findById(3L)).thenReturn(
                Optional.empty()
        );

        DriverRideStatusRequestModel driverRequestModel = new DriverRideStatusRequestModel();
        driverRequestModel.setStatus(DriverRideStatus.READY);

        ValidationFailureException validationFailureException =
                assertThrows(ValidationFailureException.class, () -> driverServiceImpl.getDriverRideStatus(3L));

        assertEquals("The field Should be" +ErrorConstant.IDENTITIER_FIELD, ErrorConstant.IDENTITIER_FIELD, validationFailureException.getErrors().get(0).getField());
        assertEquals("The message Should be" +ErrorConstant.IDENTITIER_NOT_VALID, ErrorConstant.IDENTITIER_NOT_VALID, validationFailureException.getErrors().get(0).getMessage());

    }
}
