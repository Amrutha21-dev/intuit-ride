package com.intuit.demo.enumeration;

import lombok.Getter;

@Getter
public enum UserRefreshTokenStatus implements PersistableEnum<String> {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String value;
    UserRefreshTokenStatus(String value) {
        this.value = value;
    }

    @Override
    public String getCode() {
        return value;
    }

    @jakarta.persistence.Converter
    public static class Converter extends PersistableEnumConverter<UserRefreshTokenStatus, String> {
        public Converter() {
            super(UserRefreshTokenStatus.class);
        }
    }
}
