package com.intuit.demo.enumeration;

public enum DriverStatus implements PersistableEnum<String> {
    NEW("NEW"),
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String value;
    DriverStatus(String value) {
        this.value = value;
    }

    @Override
    public String getCode() {
        return value;
    }

    @jakarta.persistence.Converter
    public static class Converter extends PersistableEnumConverter<DriverStatus, String> {
        public Converter() {
            super(DriverStatus.class);
        }
    }
}
