package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.TweetAsset;
import com.ft.api.ucm.model.v1.TweetFields;

public class TweetData extends BaseData implements AssetAware {


    private String id;
    private String tweetSource;

    @Override
    public boolean isAllRequiredDataPresent() {
        return containsValidData(id) && containsValidData(tweetSource);
    }

    @Override
    public Asset getAsset() throws BodyProcessingException {
        if(this.isAllRequiredDataPresent()) {
            TweetFields fields = new TweetFields(tweetSource, id);
            return new TweetAsset(fields);
        }
        throw new BodyProcessingException(GET_ASSET_NO_VALID_EXCEPTION_MESSAGE);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTweetSource() {
        return tweetSource;
    }

    public void setTweetSource(String tweetSource) {
        this.tweetSource = tweetSource;
    }
}
