package com.intuit.demo.enumeration;

public enum UserStatus implements PersistableEnum<String> {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String value;
    UserStatus(String value) {
        this.value = value;
    }

    @Override
    public String getCode() {
        return value;
    }

    @jakarta.persistence.Converter
    public static class Converter extends PersistableEnumConverter<UserStatus, String> {
        public Converter() {
            super(UserStatus.class);
        }
    }
}
