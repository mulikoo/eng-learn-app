package com.mulikoo.englearnapp.enums;

import lombok.Getter;

public enum UserSortField {

    USER_NAME("userName"),
    CREATION_DATE("creationDate")
    ;

    @Getter
    private final String fieldName;

    UserSortField(String fieldName) {
        this.fieldName = fieldName;
    }
}
