package com.intuit.demo.service;

import com.intuit.demo.model.JWTParametersModel;

import java.time.ZonedDateTime;

public interface JWTTokenService {

    String createJWTToken(Long id, String role,ZonedDateTime creationDate);

    JWTParametersModel decodeJWTToken(String jwtToken);
}
