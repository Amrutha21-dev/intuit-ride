package com.intuit.demo.repository;

import com.intuit.demo.entity.UserRefreshTokenEntity;
import com.intuit.demo.enumeration.UserRefreshTokenStatus;
import com.intuit.demo.enumeration.UserStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshTokenRepository extends BaseRepository<UserRefreshTokenEntity,Long> {
    UserRefreshTokenEntity findByIdAndUserRefreshTokenStatusAndUserEntity_UserStatus(Long userId,
                                                                                     UserRefreshTokenStatus userRefreshTokenStatus, UserStatus userStatus);

}
