package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import org.apache.commons.lang.StringUtils;

public class PullQuoteData {

    // Initialised with empty values
    private String quoteText = null;
    private String quoteSource = null;

    public String getQuoteText() {
        return quoteText;
    }

    public String getQuoteSource() {
        return quoteSource;
    }

    public void setQuoteText(String quoteText) {
       this.quoteText = quoteText; 
    }

    public void setQuoteSource(String quoteSource) {
        this.quoteSource = quoteSource;
    }

    public boolean isOkToRender() {
       return !StringUtils.isEmpty(this.quoteText) || !StringUtils.isEmpty(this.quoteSource);
    }

}
