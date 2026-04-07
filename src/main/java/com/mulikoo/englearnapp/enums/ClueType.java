package com.mulikoo.englearnapp.enums;

import lombok.Getter;

public enum ClueType {
    PHOTO("photo"),
    AUDIO("audio"),
    TEXT("text"),
    ;

    @Getter
    private final String description;

    ClueType(String description) {
        this.description = description;
    }

}