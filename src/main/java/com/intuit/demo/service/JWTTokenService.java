package com.intuit.demo.service;

import com.intuit.demo.model.JWTParametersModel;

import java.time.ZonedDateTime;

public interface JWTTokenService {

    public String createJWTToken(Long id, String role,ZonedDateTime creationDate);

    public JWTParametersModel decodeJWTToken(String jwtToken);
}
