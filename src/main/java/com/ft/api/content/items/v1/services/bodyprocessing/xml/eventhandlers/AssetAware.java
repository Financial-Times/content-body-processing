package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.unifiedContentModel.model.Asset;

public interface AssetAware {
    
    String GET_ASSET_NO_VALID_EXCEPTION_MESSAGE = "The object does not have sufficient data to render a valid asset. Only if the method isAllRequiredDataPresent is true will this method return a valid asset.";
    
    boolean isAllRequiredDataPresent();
    
    /**
     * This method returns an instance related to the implementing class that extends Asset for use in returning data in a response. 
     * An exception will be thrown if the class does not have sufficient data to create a valid Asset instance. 
     * @return
     * @throws IllegalStateException
     */
    Asset getAsset() throws BodyProcessingException;
}
