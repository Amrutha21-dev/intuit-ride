package com.intuit.demo.filter;


import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.entity.UserLoginInfoEntity;
import com.intuit.demo.entity.UserRefreshTokenEntity;
import com.intuit.demo.enumeration.UserRefreshTokenStatus;
import com.intuit.demo.enumeration.UserStatus;
import com.intuit.demo.exception.UnauthorisedException;
import com.intuit.demo.model.JWTParametersModel;
import com.intuit.demo.repository.UserLoginInfoRepository;
import com.intuit.demo.repository.UserRefreshTokenRepository;
import com.intuit.demo.service.JWTTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTokenService jwtTokenService;

    @Autowired
    private UserLoginInfoRepository userLoginInfoRepository;

    @Autowired
    private UserRefreshTokenRepository userRefreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws IOException, ServletException {
        String authorization = request.getHeader(AppConstant.AUTHORIZATION);
        try {
            log.info("Authorizing request");
            if(authorization == null){
                throw new UnauthorisedException("Authorization header is null");
            }
            if(!authorization.startsWith("Bearer ")){
                throw new UnauthorisedException("Authorization header is invalid");
            };
            //The actual token starts from after "Bearer"
            HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
            String jwtToken = authorization.substring(7);
            JWTParametersModel jwtParametersModel = jwtTokenService.decodeJWTToken(jwtToken);
            Optional<UserLoginInfoEntity> userLoginInfoEntityOptional =
                    userLoginInfoRepository.findById(jwtParametersModel.getUserId());
            if(userLoginInfoEntityOptional.isPresent()){
                UserLoginInfoEntity userLoginInfoEntity = userLoginInfoEntityOptional.get();
                if(!userLoginInfoEntity.getLastLoginDate().withNano(0).
                        withZoneSameInstant(AppConstant.UTC_ZONE_ID).equals(jwtParametersModel.getIssuedAt())){
                    throw new UnauthorisedException("Authorization header is invaid");
                }
                UserRefreshTokenEntity userRefreshTokenEntity = userRefreshTokenRepository.
                        findByIdAndUserRefreshTokenStatusAndUserEntity_UserStatus
                        (jwtParametersModel.getUserId(), UserRefreshTokenStatus.ACTIVE, UserStatus.ACTIVE);
                if(userRefreshTokenEntity == null){
                    throw new UnauthorisedException("Authorization header is invaid");
                }
            }
            requestWrapper.addHeader(AppConstant.X_ROLE, jwtParametersModel.getRole().getValue());
            requestWrapper.addHeader(AppConstant.X_USER_ID, jwtParametersModel.getUserId().toString());
            log.info("Authorised request");
            filterChain.doFilter(requestWrapper, response);
        }
        catch (ExpiredJwtException e){
            log.error("Unauthorised request with expired jwt token"+request.getRequestURI(),e);
            throw new UnauthorisedException("Unauthorised request with expired jwt token");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return (
                path.startsWith("/actuator")
                || "/v3/api-docs".equals(path)
                || "/v3/api-docs/swagger-config".equals(path)
                || "/swagger-ui/index.html".equals(path)
                || "/swagger-ui/swagger-initializer.js".equals(path)
                || "/swagger-ui/swagger-ui-bundle.js".equals(path)
                || "/swagger-ui/swagger-ui-standalone-preset.js".equals(path)
                || "/swagger-ui/index.css".equals(path)
                || "/swagger-ui/swagger-ui.css".equals(path)
                || path.equals("/v1/driver/register")
                || path.equals("/v1/admin/register")
                || path.equals("/v1/login")
                || path.equals("/v1/forgot-password")
                || path.equals("/v1/set-new-password")
                || path.equals("/v1/driver/background-verification/external/status")
        );
    }
}
