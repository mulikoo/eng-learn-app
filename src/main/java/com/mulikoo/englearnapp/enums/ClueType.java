package com.mulikoo.englearnapp.enums;

import lombok.Getter;

public enum ClueType {
    HINT("hint"),
    TRANSLATION("translation");

    @Getter
    private final String description;

    ClueType(String description) {
        this.description = description;
    }

}