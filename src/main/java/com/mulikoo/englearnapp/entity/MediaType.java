package com.mulikoo.englearnapp.entity;

public enum MediaType {
    AUDIO("audio"),
    PHOTO("image"),
    ;

    private final String description;

    MediaType(String description) {
        this.description = description;
    }


}
