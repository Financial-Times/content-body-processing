package com.ft.content.bodyprocessing;

import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.Image;
import com.ft.api.ucm.model.v1.TypeBasedImage;

import java.util.List;
import java.util.Map;

public class NoopBodyProcessingContext implements BodyProcessingContext {

    @Override
    public String getAttributeForImage(String attributeName, String uuid) {
        return null;
    }

    @Override
    public String getAttributeForImage(ImageAttribute imageAttribute, String uuid) {
        return null;
    }

    @Override
    public Asset assignAssetNameToExistingAsset(String uuid) {
        return null;
    }

    @Override
    public Asset addAsset(Asset asset) {
        return null;
    }

    @Override
    public boolean imageExists(String uuid) {
        return false;
    }

    @Override
    public void addImageWithAttributes(Map<String, String> attributes, TypeBasedImage.ImageType imageType) {
    }

    @Override
    public void addAttributesToExistingImageWithId(String imageId, Map<String, String> attributes) {
    }

    @Override
    public List<Image> retrieveProcessedImages() {
        return null;
    }
}
