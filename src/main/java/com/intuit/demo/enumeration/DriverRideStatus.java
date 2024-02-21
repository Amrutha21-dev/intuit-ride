package com.intuit.demo.enumeration;

public enum DriverRideStatus implements PersistableEnum<String> {
    READY("READY"),
    NOT_READY("NOT_READY");

    private final String value;
    DriverRideStatus(String value) {
        this.value = value;
    }

    @Override
    public String getCode() {
        return value;
    }

    @jakarta.persistence.Converter
    public static class Converter extends PersistableEnumConverter<DriverRideStatus, String> {
        public Converter() {
            super(DriverRideStatus.class);
        }
    }
}
