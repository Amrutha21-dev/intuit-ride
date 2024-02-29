package com.intuit.demo.service.impl;

import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.enumeration.UserType;
import com.intuit.demo.model.JWTParametersModel;
import com.intuit.demo.service.JWTTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTTokenServiceImpl implements JWTTokenService {

    private final Key key;

    public JWTTokenServiceImpl(@Value("${jwt.token.sign-key}") String signKey) {
        key = new SecretKeySpec(Base64.getDecoder().decode(signKey), SignatureAlgorithm.HS256.getJcaName());
    }

    public String createJWTToken(Long id, String role,ZonedDateTime creationDate) {
        return Jwts.builder().setSubject(role)
                .setId(id.toString())
                .setExpiration(Date.from(creationDate.plusDays(1).toInstant()))
                .setIssuedAt(Date.from(creationDate.toInstant()))
                .signWith(key).compact();
    }

    public JWTParametersModel decodeJWTToken(String jwtToken) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
        Long userId = Long.valueOf(claims.getId());
        UserType userType = UserType.valueOf(claims.getSubject());
        ZonedDateTime issuedAt = claims.getIssuedAt().toInstant().atZone(AppConstant.UTC_ZONE_ID);
        ZonedDateTime expiredAt = claims.getExpiration().toInstant().atZone(AppConstant.UTC_ZONE_ID);
        return new JWTParametersModel(userId, userType, issuedAt, expiredAt);
    }
}
