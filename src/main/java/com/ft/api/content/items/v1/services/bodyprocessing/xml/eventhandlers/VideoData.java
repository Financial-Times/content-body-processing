package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.VideoAsset;
import com.ft.api.ucm.model.v1.VideoFields;

public class VideoData extends BaseData implements AssetAware  {

    private static final String VIDEO_SOURCE = "Brightcove";
    private String id;

    @Override
    public boolean isAllRequiredDataPresent() {
        return containsValidData(this.id);
    }

    @Override
    public Asset getAsset() throws BodyProcessingException {
        if(this.isAllRequiredDataPresent()) {
            VideoFields fields = new VideoFields(VIDEO_SOURCE, this.id);
            return new VideoAsset(fields);
        }
        throw new BodyProcessingException(GET_ASSET_NO_VALID_EXCEPTION_MESSAGE);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
