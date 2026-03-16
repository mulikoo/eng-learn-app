package com.mulikoo.englearnapp.enums;

import lombok.Getter;

public enum CategorySortField {

    NAME("name"),
    CREATION_DATE("creationDate")
    ;

    @Getter
    private final String fieldName;

    CategorySortField(String fieldName) {
        this.fieldName = fieldName;
    }
}
