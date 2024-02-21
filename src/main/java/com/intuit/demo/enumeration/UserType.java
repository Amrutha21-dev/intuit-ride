package com.intuit.demo.enumeration;

import lombok.Getter;

@Getter
public enum UserType implements PersistableEnum<String> {
    USER("USER"),
    DRIVER("DRIVER"),
    ADMIN("ADMIN");

    private final String value;
    UserType(String value) {
        this.value = value;
    }

    @Override
    public String getCode() {
        return value;
    }

    @jakarta.persistence.Converter
    public static class Converter extends PersistableEnumConverter<UserType, String> {
        public Converter() {
            super(UserType.class);
        }
    }
}
