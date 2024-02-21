package com.intuit.demo.repository;

import com.intuit.demo.entity.DriverBackgroundVerificationEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverBackgroundVerificationRepository extends BaseRepository<DriverBackgroundVerificationEntity,Long> {

    DriverBackgroundVerificationEntity findByDriverEntity_AdhaarNumber(String aadhaarNumber);
}
