package com.mulikoo.englearnapp.enums;

import lombok.Getter;

public enum WordSortField {

    NAME("name"),
    CREATION_DATE("creationDate")
    ;

    @Getter
    private final String fieldName;

    WordSortField(String fieldName) {
        this.fieldName = fieldName;
    }
}
