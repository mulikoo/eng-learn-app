package com.mulikoo.englearnapp.enums;

import lombok.Getter;

public enum MediaType {
    AUDIO("audio"),
    PHOTO("image"),
    ;

    @Getter
    private final String description;

    MediaType(String description) {
        this.description = description;
    }


}
