package com.intuit.demo.repository;

import com.intuit.demo.entity.UserLoginInfoEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginInfoRepository extends BaseRepository<UserLoginInfoEntity,Long> {

}
