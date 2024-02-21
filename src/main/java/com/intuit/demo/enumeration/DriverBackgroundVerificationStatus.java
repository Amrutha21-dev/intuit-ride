package com.intuit.demo.enumeration;

public enum DriverBackgroundVerificationStatus implements PersistableEnum<String> {
    SENT_FOR_VERIFICATION("SENT_FOR_VERIFICATION"),
    VERIFICATION_SUCCESSFUL("VERIFICATION_SUCCESSFUL"),
    VERIFICATION_FAILED("VERIFICATION_FAILED");

    private final String value;
    DriverBackgroundVerificationStatus(String value) {
        this.value = value;
    }

    @Override
    public String getCode() {
        return value;
    }

    @jakarta.persistence.Converter
    public static class Converter extends PersistableEnumConverter<DriverBackgroundVerificationStatus, String> {
        public Converter() {
            super(DriverBackgroundVerificationStatus.class);
        }
    }
}
