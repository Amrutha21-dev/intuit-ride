package com.intuit.demo.model;

import com.intuit.demo.enumeration.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class JWTParametersModel {

    private Long userId;
    private UserType role;
    private ZonedDateTime issuedAt;
    private ZonedDateTime expireAt;

    public JWTParametersModel(Long userId, UserType userType, ZonedDateTime issuedAt, ZonedDateTime expireAt) {
        this.userId = userId;
        this.role = userType;
        this.issuedAt = issuedAt;
        this.expireAt = expireAt;
    }
}
