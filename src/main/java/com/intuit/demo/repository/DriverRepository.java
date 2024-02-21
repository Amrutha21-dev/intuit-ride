package com.intuit.demo.repository;

import com.intuit.demo.entity.DriverEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends BaseRepository<DriverEntity,Long> {
}
