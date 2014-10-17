package com.ft.content.bodyprocessing.xml.eventhandlers;

import org.apache.commons.lang.StringUtils;

public abstract class BaseData {

    protected String nullIfEmpty(String value) {
        if(StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    protected boolean containsValidData(String data) {
        return !StringUtils.isBlank(data);
    }
}
