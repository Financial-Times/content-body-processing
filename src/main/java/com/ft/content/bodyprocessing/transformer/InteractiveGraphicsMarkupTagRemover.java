package com.ft.content.bodyprocessing.transformer;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

public class InteractiveGraphicsMarkupTagRemover implements FieldTransformer {
    
    private static final String INTERACTIVE_GRAPHICS_MARKUP_TAG_REGEX = "(?s)<div[\\s]*class=\"interactive-comp\">(.*?)</div>";
    private static final Pattern IMG_TAG_PATTERN = Pattern.compile(INTERACTIVE_GRAPHICS_MARKUP_TAG_REGEX);

    @Override
    public String transform(String fieldValue) throws TransformationException {
        if (StringUtils.isEmpty(fieldValue)) {
            return null;
        }
        return IMG_TAG_PATTERN.matcher(fieldValue).replaceFirst("").trim();
    }
}
