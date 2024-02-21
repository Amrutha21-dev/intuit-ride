package com.intuit.demo.repository;

import com.intuit.demo.entity.UserPasswordResetTokenEntity;
import com.intuit.demo.enumeration.UserStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordResetTokenRepository extends BaseRepository<UserPasswordResetTokenEntity,Long> {

    UserPasswordResetTokenEntity findByIdAndUserEntity_UserStatus(Long userId, UserStatus userStatus);
}
