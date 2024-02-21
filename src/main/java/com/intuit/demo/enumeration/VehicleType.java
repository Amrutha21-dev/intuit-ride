package com.intuit.demo.enumeration;

import lombok.Getter;

@Getter
public enum VehicleType implements PersistableEnum<String> {
    COMPACT_CAR("COMPACT_CAR"),
    SEDAN_CAR("SEDAN_CAR"),
    SUV_CAR("SUV_CAR"),
    AUTO("AUTO"),
    BIKE("BIKE");

    private final String value;
    VehicleType(String value) {
        this.value = value;
    }

    @Override
    public String getCode() {
        return value;
    }

    @jakarta.persistence.Converter
    public static class Converter extends PersistableEnumConverter<VehicleType, String> {
        public Converter() {
            super(VehicleType.class);
        }
    }
}
