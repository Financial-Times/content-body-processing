package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import org.apache.commons.lang.StringUtils;

public abstract class BaseData {

    protected String nullIfEmpty(String value) {
        if(StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

}
