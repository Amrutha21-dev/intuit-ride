package com.intuit.demo.repository;

import com.intuit.demo.entity.UserEntity;
import com.intuit.demo.enumeration.UserStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {

    UserEntity findByEmailAndUserStatus(String email, UserStatus userStatus);
}
