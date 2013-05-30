package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.api.ucm.model.v1.Asset;

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
