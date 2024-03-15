package org.royllo.explorer.core.util.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * String list converter.
 */
@Converter
public final class StringListConverter implements AttributeConverter<List<String>, String> {

    /** Split character. */
    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(final List<String> stringList) {
        if (stringList != null) {
            return String.join(SPLIT_CHAR, stringList);
        } else {
            return "";
        }
    }

    @Override
    public List<String> convertToEntityAttribute(final String string) {
        if (string != null && !string.isEmpty()) {
            return Arrays.asList(string.split(SPLIT_CHAR));
        } else {
            return emptyList();
        }
    }

}
