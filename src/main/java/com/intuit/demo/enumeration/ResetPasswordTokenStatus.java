package com.intuit.demo.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResetPasswordTokenStatus implements PersistableEnum<String> {

    NEW("NEW"),USED("USED"),EXPIRED("EXPIRED");


    private final String value;

    @Override
    public String getCode() {
        return value;
    }

    @jakarta.persistence.Converter
    public static class Converter extends PersistableEnumConverter<ResetPasswordTokenStatus, String> {
        public Converter() {
            super(ResetPasswordTokenStatus.class);
        }
    }
}
