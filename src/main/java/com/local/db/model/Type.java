package com.local.db.model;

import java.util.Arrays;
import java.util.Optional;

public enum Type {
    INTEGER,
    REAL,
    CHAR,
    STRING,
    COLOR,
    COLORInvl;

    public static Optional<Type> getIfPresent(String str) {
        return Arrays.stream(Type.values())
                .filter(type -> str.trim().toLowerCase().equals(type.name().toLowerCase()))
                .findFirst();
    }
}

