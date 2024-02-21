package com.intuit.demo.repository;

import com.intuit.demo.entity.UserAuthenticationEntity;
import com.intuit.demo.enumeration.UserStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthenticationRepository extends BaseRepository<UserAuthenticationEntity,Long> {

    UserAuthenticationEntity findByIdAndUserEntity_UserStatus(Long userId, UserStatus userStatus);

}
