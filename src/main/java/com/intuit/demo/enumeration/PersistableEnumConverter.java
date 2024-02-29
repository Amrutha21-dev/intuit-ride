package com.intuit.demo.enumeration;


import jakarta.persistence.AttributeConverter;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class PersistableEnumConverter <T extends PersistableEnum<E>, E> implements AttributeConverter<T, E> {
    private final Class<T> clazz;

    @Override
    public E convertToDatabaseColumn(T attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public T convertToEntityAttribute(E dbData) {
        if(dbData != null) {
            T[] enums = clazz.getEnumConstants();

            for (T e : enums) {
                if (e.getCode().equals(dbData)) {
                    return e;
                }
            }

            throw new UnsupportedOperationException();
        }
        return null;
    }
}
