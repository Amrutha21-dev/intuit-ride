package com.intuit.demo.constant;

import java.time.ZoneId;

public class AppConstant {

    public static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    public static final String PHONE_NUMBER_REGEX = "^\\d{10}$";
    public static final String ADHAAR_NUMBER_REGEX = "^\\d{12}$";
    public static final String X_SOURCE_IP_ADDRESS = "X-SourceIpAddress";
    public static final String X_USER_ID = "X-UserId";
    public static final String X_ROLE = "X-Role";
    public static final String AUTHORIZATION = "Authorization";
    public static final String APPLICATION_JSON = "application/json";
    public static final int MIN_PASSWORD_LENGTH = 6;

}
