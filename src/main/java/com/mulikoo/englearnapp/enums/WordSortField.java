package com.mulikoo.englearnapp.enums;

import lombok.Getter;

public enum WordSortField {

    NAME("name"),
    CREATION_DATE("creationDate"),
    TRANSLATION ("translation")
    ;

    @Getter
    private final String fieldName;

    WordSortField(String fieldName) {
        this.fieldName = fieldName;
    }
}
