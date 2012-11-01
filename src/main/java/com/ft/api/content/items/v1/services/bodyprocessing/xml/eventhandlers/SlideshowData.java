package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.unifiedContentModel.model.Asset;

public class SlideshowData extends BaseData implements AssetAware {

    private String uuid;
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean isAllRequiredDataPresent() {
        return containsValidData(this.uuid);
    }

    @Override
    public Asset getAsset() throws BodyProcessingException {
        // No needed for this handler since all assets are pre-loaded with slideshow assets prior to handler execution.
        return null;
    }

}
