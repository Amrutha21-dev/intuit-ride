package com.intuit.demo.entity;

import com.intuit.demo.constant.AppConstant;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseWithDateEntity implements BaseEntity {

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @PreUpdate
    public void setUpdatedDate(){
        setUpdatedAt(ZonedDateTime.now(AppConstant.UTC_ZONE_ID));
    }

    @PrePersist
    public void setCreatedAndUpdatedDates(){
        setCreatedAt(ZonedDateTime.now(AppConstant.UTC_ZONE_ID));
        setUpdatedAt(ZonedDateTime.now(AppConstant.UTC_ZONE_ID));
    }

}
