package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import org.apache.commons.lang.StringUtils;

import com.ft.unifiedContentModel.model.Asset;
import com.ft.unifiedContentModel.model.PullQuote;
import com.ft.unifiedContentModel.model.PullQuoteFields;

public class PullQuoteData extends BaseData implements AssetAware {

    private String quoteText;
    private String quoteSource;

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

    @Override
    public boolean isAllRequiredDataPresent() {
       return containsValidData(this.quoteText) || containsValidData(this.quoteSource);
    }

    private boolean containsValidData(String data) {
        return !StringUtils.isBlank(data) && !StringUtils.isEmpty(data);
    }

    @Override
    public Asset getAsset() {
       PullQuote pullQuote = null;
       if(this.isAllRequiredDataPresent()) {
           pullQuote = new PullQuote();
           PullQuoteFields fields = new PullQuoteFields(nullIfEmpty(this.quoteText), nullIfEmpty(this.quoteSource));
           pullQuote.setFields(fields);
           return pullQuote;
       }
       throw new IllegalStateException("The object does not have sufficient data to render a valid asset. Only if the method isOkToRender is true will this method return a valid asset.");
    }

}
