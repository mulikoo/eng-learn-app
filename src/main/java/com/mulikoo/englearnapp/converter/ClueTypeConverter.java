package com.mulikoo.englearnapp.converter;

import com.mulikoo.englearnapp.enums.ClueType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class ClueTypeConverter implements AttributeConverter<Set<ClueType>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(Set<ClueType> attribute) {
        if (CollectionUtils.isEmpty(attribute)) {
            return null;
        }
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public Set<ClueType> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(dbData.split(SEPARATOR))
                .map(ClueType::valueOf)
                .collect(Collectors.toSet());
    }
}
