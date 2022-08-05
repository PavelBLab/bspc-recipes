package com.cookbook.recipes.mapper.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class TransformHelper {

    private static final String REGEX = "[&<>]";
    private static final String REPLACEMENT = "";

    public String sanitize(final String toBeSanitized) {
        if (StringUtils.isNotBlank(toBeSanitized)) {
            return toBeSanitized.replaceAll(REGEX, REPLACEMENT);
        }
        return toBeSanitized;
    }
}
