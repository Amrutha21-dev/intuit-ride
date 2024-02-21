package com.intuit.demo.service.impl;

import com.intuit.demo.constant.AppConstant;
import com.intuit.demo.constant.ErrorConstant;
import com.intuit.demo.entity.*;
import com.intuit.demo.enumeration.ResetPasswordTokenStatus;
import com.intuit.demo.enumeration.UserRefreshTokenStatus;
import com.intuit.demo.enumeration.UserStatus;
import com.intuit.demo.enumeration.UserType;
import com.intuit.demo.exception.ApiFailureException;
import com.intuit.demo.exception.ValidationFailureException;
import com.intuit.demo.model.request.*;
import com.intuit.demo.model.response.EmptyResponse;
import com.intuit.demo.model.response.Error;
import com.intuit.demo.model.response.LoginResponseModel;
import com.intuit.demo.repository.*;
import com.intuit.demo.service.AuthService;
import com.intuit.demo.service.CryptoService;
import com.intuit.demo.service.JWTTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private UserRefreshTokenRepository userRefreshTokenRepository;

    @Autowired
    private UserLoginInfoRepository userLoginInfoRepository;

    @Autowired
    private UserPasswordResetTokenRepository userPasswordResetTokenRepository;

    @Autowired
    private JWTTokenService jwtTokenService;

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public LoginResponseModel registerDriver(UserRequestModel userRequestModel, String ip) {
        List<Error> errorList = new ArrayList<>();
        if(checkIfEmailIsRegistered(userRequestModel.getEmail())) {
            errorList.add(new Error(ErrorConstant.EMAIL_FIELD, ErrorConstant.EMAIL_ALREADY_EXISTS));
        }
        else{
            UserEntity userEntity = createNewUser(userRequestModel);
            createNewUserAuthenticationEntity(userEntity, userRequestModel.getPassword());
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setId(userEntity.getId());
            userRoleEntity.setRole(UserType.DRIVER);
            userRoleRepository.save(userRoleEntity);
            return generateLoginTokens(userEntity.getId(),ip);
        }
        throw new ValidationFailureException(errorList);
    }

    @Override
    @Transactional
    public LoginResponseModel registerAdmin(UserRequestModel userRequestModel, String ip) {
        List<Error> errorList = new ArrayList<>();
        if(checkIfEmailIsRegistered(userRequestModel.getEmail())) {
            errorList.add(new Error(ErrorConstant.EMAIL_FIELD, ErrorConstant.EMAIL_ALREADY_EXISTS));
        }
        else{
            UserEntity userEntity = createNewUser(userRequestModel);
            createNewUserAuthenticationEntity(userEntity, userRequestModel.getPassword());
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setId(userEntity.getId());
            userRoleEntity.setRole(UserType.ADMIN);
            userRoleRepository.save(userRoleEntity);
            return generateLoginTokens(userEntity.getId(),ip);
        }
        throw new ValidationFailureException(errorList);
    }

    @Override
    @Transactional
    public LoginResponseModel login(LoginRequestModel loginRequestModel, String ip) {
        List<Error> errorList = new ArrayList<>();
        String password = loginRequestModel.getPassword();
        if (!checkIfEmailIsRegistered(loginRequestModel.getEmail())) {
            errorList.add(new Error(ErrorConstant.EMAIL_FIELD,ErrorConstant.EMAIL_NOT_REGISTERED));
        } else {
            UserEntity userEntity = userRepository.
                    findByEmailAndUserStatus(loginRequestModel.getEmail().toLowerCase(),
                            UserStatus.ACTIVE);
            if(userEntity == null){
                log.error("User not allowed to login");
                throw new ApiFailureException("User not allowed to login");
            }
            Long userId = userEntity.getId();
            Optional<UserAuthenticationEntity> userAuthenticationEntityOptional
                    = userAuthenticationRepository.findById(userEntity.getId());
            if(userAuthenticationEntityOptional.isEmpty() ||
                    userAuthenticationEntityOptional.get().getEncryptedPassword() == null) {
                log.error("Password not found for user "+userId);
                throw new ApiFailureException("Password not found for user "+userId);
            }
            else {
                UserAuthenticationEntity userAuthenticationEntity = userAuthenticationEntityOptional.get();
                String encryptedPassword = userAuthenticationEntity.getEncryptedPassword();
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                if (!bCryptPasswordEncoder.matches(password, encryptedPassword)) {
                    errorList.add(new Error(ErrorConstant.PASSWORD_FIELD, ErrorConstant.USER_AND_PASSWORD_DOESNT_MATCH));
                } else {
                    return generateLoginTokens(userId,ip);
                }
            }
        }
        throw new ValidationFailureException(errorList);
    }

    @Override
    @Transactional
    public LoginResponseModel refreshJwtToken(Long userId, RefreshJWTTokenRequestModel refreshJWTTokenRequestModel, String ip) {
        List<Error> errorList = new ArrayList<>();
        String refreshToken = refreshJWTTokenRequestModel.getRefreshToken();
        UserRefreshTokenEntity userRefreshTokenEntity = userRefreshTokenRepository.
                findByIdAndUserRefreshTokenStatusAndUserEntity_UserStatus(userId,UserRefreshTokenStatus.ACTIVE,UserStatus.ACTIVE);
        if(userRefreshTokenEntity == null){
            errorList.add(new Error(ErrorConstant.REFRESH_TOKEN_FIELD, ErrorConstant.REFRESH_TOKEN_NOT_FOUND));
        } else {
            if(checkIfRefreshTokenExpired(userRefreshTokenEntity)){
                errorList.add(new Error(ErrorConstant.REFRESH_TOKEN_FIELD, ErrorConstant.REFRESH_TOKEN_EXPIRED));
            } else {
                String token = userRefreshTokenEntity.getRefreshToken();
                if(checkIfTokensAreNotMatchingMatching(token, refreshToken)){
                    errorList.add(new Error(ErrorConstant.REFRESH_TOKEN_FIELD, ErrorConstant.REFRESH_TOKEN_NOT_MATCHING));
                } else {
                    Optional<UserRefreshTokenEntity> userRefreshTokenEntityOptional = userRefreshTokenRepository.findById(userId);
                    if(userRefreshTokenEntityOptional.isPresent() && userRefreshTokenEntityOptional.get().
                            getUserRefreshTokenStatus().equals(UserRefreshTokenStatus.INACTIVE)){
                        errorList.add(new Error(ErrorConstant.REFRESH_TOKEN_FIELD, ErrorConstant.FORCE_LOG_OUT_USER));
                    }
                    else {
                        return generateLoginTokens(userId,ip);
                    }
                }
            }
        }
        throw new ValidationFailureException(errorList);
    }

    @Override
    @Transactional
    public EmptyResponse setNewPassword(NewPasswordRequestModel newPasswordRequestModel) {
        List<Error> errorList = new ArrayList<>();
        EmptyResponse emptyResponse = new EmptyResponse();
        final String newPassword = newPasswordRequestModel.getNewPassword();
        String token = newPasswordRequestModel.getSha256Token();
        String encryptedUserId = token.split("\\.",2)[1];
        String resetPasswordToken = token.split("\\.",2)[0];
        String userIdString = cryptoService.decrypt(encryptedUserId);
        Long userIdFromLink = Long.parseLong(userIdString);
        UserPasswordResetTokenEntity userPasswordResetTokenEntity = userPasswordResetTokenRepository.
                findByIdAndUserEntity_UserStatus(userIdFromLink, UserStatus.ACTIVE);
        if (userPasswordResetTokenEntity == null) {
            errorList.add(new Error(ErrorConstant.PASSWORD_TOKEN_FIELD, ErrorConstant.RESET_TOKEN_NOT_FOUND));
        } else {
            if (checkIfTokensAreNotMatchingMatching(userPasswordResetTokenEntity.getResetPasswordToken(), resetPasswordToken)) {
                errorList.add(new Error(ErrorConstant.PASSWORD_TOKEN_FIELD, ErrorConstant.PASSWORD_TOKEN_NO_MATCH));
            }
            else if(ResetPasswordTokenStatus.USED.equals(userPasswordResetTokenEntity.getResetPasswordTokenStatus())){
                errorList.add(new Error(ErrorConstant.PASSWORD_TOKEN_FIELD, ErrorConstant.PASSWORD_TOKEN_USED));
            }
            else if(ResetPasswordTokenStatus.EXPIRED.equals(userPasswordResetTokenEntity.getResetPasswordTokenStatus())){
                errorList.add(new Error(ErrorConstant.PASSWORD_TOKEN_FIELD, ErrorConstant.PASSWORD_TOKEN_EXPIRED));
            }
            else if (validateIfTokenIsExpiredAndExpireIt(userPasswordResetTokenEntity)) {
                errorList.add(new Error(ErrorConstant.PASSWORD_TOKEN_FIELD, ErrorConstant.PASSWORD_TOKEN_EXPIRED));
            }
            else {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                final String hashedPassword = bCryptPasswordEncoder.encode(newPassword);
                UserAuthenticationEntity userAuthenticationEntity
                        = userAuthenticationRepository.findByIdAndUserEntity_UserStatus(userIdFromLink, UserStatus.ACTIVE);
                if(userAuthenticationEntity == null) {
                    log.error("Password not found for user "+userIdFromLink);
                    throw new ApiFailureException("Password not found for user "+userIdFromLink);
                }
                else{
                    userAuthenticationEntity.setEncryptedPassword(hashedPassword);
                    userAuthenticationRepository.save(userAuthenticationEntity);
                    setPasswordTokenAsUsed(userPasswordResetTokenEntity);
                    UserRefreshTokenEntity userRefreshTokenEntity =
                    userRefreshTokenRepository.findByIdAndUserRefreshTokenStatusAndUserEntity_UserStatus
                            (userIdFromLink,UserRefreshTokenStatus.ACTIVE,UserStatus.ACTIVE);
                    invalidateRefreshToken(userRefreshTokenEntity);
                }
            }
        }
        if (!errorList.isEmpty()) {
            throw new ValidationFailureException(errorList);
        }
        return emptyResponse;
    }

    @Override
    @Transactional
    public EmptyResponse sendResetPasswordToken(ResetPasswordRequestModel resetPasswordRequestModel) {
        List<Error> errorList = new ArrayList<>();
        EmptyResponse emptyResponse = new EmptyResponse();
        if (!checkIfEmailIsRegistered(resetPasswordRequestModel.getEmail())) {
            errorList.add(new Error(ErrorConstant.EMAIL_FIELD,ErrorConstant.EMAIL_NOT_REGISTERED));
        }
        else{
            UserEntity userEntity = userRepository.
                    findByEmailAndUserStatus(resetPasswordRequestModel.getEmail(),UserStatus.ACTIVE);
            checkIfNewUserAuthenticationExistsAndExpireIt(userEntity.getId());
            String passwordResetToken =
                    createNewPasswordResetToken(userEntity);
            String encryptedUserId = cryptoService.encrypt(userEntity.getId().toString());
            String encryptedToken = passwordResetToken + "." + encryptedUserId;
            log.info("encryptedToken "+encryptedToken);
            //todo send email
        }
        if (!errorList.isEmpty()) {
            throw new ValidationFailureException(errorList);
        }
        return emptyResponse;
    }

    @Override
    @Transactional
    public EmptyResponse logout(Long userId) {
        List<Error> errorList = new ArrayList<>();
        EmptyResponse emptyResponse = new EmptyResponse();
        UserRefreshTokenEntity userRefreshTokenEntity = userRefreshTokenRepository.
                findByIdAndUserRefreshTokenStatusAndUserEntity_UserStatus(userId,UserRefreshTokenStatus.ACTIVE,UserStatus.ACTIVE);
        if(userRefreshTokenEntity == null){
            errorList.add(new Error(ErrorConstant.REFRESH_TOKEN_FIELD,ErrorConstant.REFRESH_TOKEN_NOT_FOUND));
        }
        else{
            invalidateRefreshToken(userRefreshTokenEntity);
        }
        if (!errorList.isEmpty()) {
            throw new ValidationFailureException(errorList);
        }
        return emptyResponse;
    }

    private boolean checkIfEmailIsRegistered(String email){
        UserEntity userEntity = userRepository.
                findByEmailAndUserStatus(email, UserStatus.ACTIVE);
        return (userEntity != null);
    }

    private UserEntity createNewUser(UserRequestModel userRequestModel){
        UserEntity userEntity = UserEntity.builder()
                .email(userRequestModel.getEmail())
                .userStatus(UserStatus.ACTIVE)
                .build();
        return userRepository.save(userEntity);
    }

    private void createNewUserAuthenticationEntity(UserEntity userEntity, String password){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        final String hashedPassword = bCryptPasswordEncoder.encode(password);
        UserAuthenticationEntity userAuthenticationEntity = new UserAuthenticationEntity(userEntity.getId(),
                hashedPassword);
        userAuthenticationRepository.save(userAuthenticationEntity);
    }

    private LoginResponseModel generateLoginTokens(Long userId, String ip){
        Optional<UserRoleEntity> userRoleEntityOptional = userRoleRepository.findById(userId);
        ZonedDateTime creationDate = ZonedDateTime.now(ZoneId.of("UTC"));
        setLoginInfo(userId,ip,creationDate);
        String token = jwtTokenService.createJWTToken(userId, userRoleEntityOptional.get().getRole().getValue(),creationDate);
        String refreshToken = createRefreshToken(userId,creationDate);
        return LoginResponseModel.builder().
                refreshToken(refreshToken).jwtToken(token).userId(userId).build();
    }

    private String createRefreshToken(Long userId, ZonedDateTime creationDate) {
        UUID uuid = UUID.randomUUID();
        String randomUUID = String.valueOf(uuid);
        Optional<UserRefreshTokenEntity> userRefreshTokenEntityOptional=
                userRefreshTokenRepository.findById(userId);
        UserRefreshTokenEntity userRefreshTokenEntity;
        if(userRefreshTokenEntityOptional.isPresent()){
            userRefreshTokenEntity = userRefreshTokenEntityOptional.get();
        } else {
            userRefreshTokenEntity = new UserRefreshTokenEntity();
            userRefreshTokenEntity.setId(userId);
        }
        userRefreshTokenEntity.setRefreshToken(randomUUID);
        userRefreshTokenEntity.setRefreshTokenGeneratedAt(creationDate);
        userRefreshTokenEntity.setUserRefreshTokenStatus(UserRefreshTokenStatus.ACTIVE);
        long refreshTokenExpireDay = 1L;

        userRefreshTokenEntity.setRefreshTokenExpiresAt(creationDate.plusDays(refreshTokenExpireDay));
        userRefreshTokenRepository.save(userRefreshTokenEntity);
        return randomUUID;
    }

    private void setLoginInfo(Long userId, String ip, ZonedDateTime creationDate){
        Optional<UserLoginInfoEntity> userLoginInfoEntityOptional = userLoginInfoRepository.findById(userId);
        UserLoginInfoEntity userLoginInfoEntity = null;
        if(userLoginInfoEntityOptional.isPresent()) {
            userLoginInfoEntity = userLoginInfoEntityOptional.get();
            userLoginInfoEntity.setLoginCount(userLoginInfoEntity.getLoginCount() + 1);
        } else {
            userLoginInfoEntity = new UserLoginInfoEntity(userId);
        }
        userLoginInfoEntity.setLastLoginIp(ip);
        userLoginInfoEntity.setLastLoginDate(creationDate);
        userLoginInfoRepository.save(userLoginInfoEntity);
    }

    private void invalidateRefreshToken(UserRefreshTokenEntity userRefreshTokenEntity){
        userRefreshTokenEntity.setUserRefreshTokenStatus(UserRefreshTokenStatus.INACTIVE);
        userRefreshTokenRepository.save(userRefreshTokenEntity);
    }

    private boolean checkIfRefreshTokenExpired(UserRefreshTokenEntity userRefreshTokenEntity){
        ZonedDateTime expireTime = userRefreshTokenEntity.getRefreshTokenExpiresAt();
        ZonedDateTime nowTime = ZonedDateTime.now(AppConstant.UTC_ZONE_ID);
        return expireTime.isBefore(nowTime);
    }

    private boolean checkIfTokensAreNotMatchingMatching(String actualToken, String tokenSent){
        return !actualToken.equals(tokenSent);
    }

    private boolean validateIfTokenIsExpiredAndExpireIt(UserPasswordResetTokenEntity userPasswordResetTokenEntity) {
        ZonedDateTime expireTime = userPasswordResetTokenEntity.getResetPasswordTokenExpiresAt();
        if (expireTime.isBefore(ZonedDateTime.now(AppConstant.UTC_ZONE_ID))){
            userPasswordResetTokenEntity.setResetPasswordTokenStatus(ResetPasswordTokenStatus.EXPIRED);
            userPasswordResetTokenRepository.save(userPasswordResetTokenEntity);
            return true;
        }
        return false;
    }

    private void setPasswordTokenAsUsed(UserPasswordResetTokenEntity userPasswordResetTokenEntity){
        userPasswordResetTokenEntity.setResetPasswordTokenStatus(ResetPasswordTokenStatus.USED);
        userPasswordResetTokenRepository.save(userPasswordResetTokenEntity);
    }

    private void checkIfNewUserAuthenticationExistsAndExpireIt(Long userId){
        UserPasswordResetTokenEntity userPasswordResetTokenEntity =
                userPasswordResetTokenRepository.findByIdAndUserEntity_UserStatus(userId, UserStatus.ACTIVE);
        if(userPasswordResetTokenEntity != null){
            if(ResetPasswordTokenStatus.NEW.equals(userPasswordResetTokenEntity.getResetPasswordTokenStatus())){
                userPasswordResetTokenEntity.setResetPasswordTokenStatus(ResetPasswordTokenStatus.EXPIRED);
                userPasswordResetTokenRepository.save(userPasswordResetTokenEntity);
            }
        }
    }

    private String createNewPasswordResetToken(UserEntity userEntity){
        UserPasswordResetTokenEntity userPasswordResetTokenEntity =
                userPasswordResetTokenRepository.findByIdAndUserEntity_UserStatus(userEntity.getId(), UserStatus.ACTIVE);
        if(userPasswordResetTokenEntity == null) {
            userPasswordResetTokenEntity = new UserPasswordResetTokenEntity();
            userPasswordResetTokenEntity.setId(userEntity.getId());
        }
        UUID uuid = UUID.randomUUID();
        String randomUUID = String.valueOf(uuid);
        ZonedDateTime currentTime = ZonedDateTime.now(AppConstant.UTC_ZONE_ID);
        userPasswordResetTokenEntity.setResetPasswordToken(randomUUID);
        userPasswordResetTokenEntity.setResetPasswordTokenGeneratedAt(currentTime);
        userPasswordResetTokenEntity.setResetPasswordTokenExpiresAt(currentTime.plusSeconds(120));
        userPasswordResetTokenEntity.setResetPasswordTokenStatus(ResetPasswordTokenStatus.NEW);
        userPasswordResetTokenRepository.save(userPasswordResetTokenEntity);
        return randomUUID;
    }

}
