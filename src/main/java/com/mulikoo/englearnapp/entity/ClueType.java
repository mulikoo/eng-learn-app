package com.mulikoo.englearnapp.entity;

public enum ClueType {
    HINT("hint"),
    TRANSLATION("translation");

    private final String description;

    ClueType(String description) {
        this.description = description;
    }

}