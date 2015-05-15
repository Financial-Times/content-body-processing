package com.ft.content.bodyprocessing;

import java.util.List;
import java.util.Map;

import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.Image;
import com.ft.api.ucm.model.v1.TypeBasedImage;


public interface BodyProcessingContext {

    @Deprecated
	public String getAttributeForImage(String attributeName, String uuid);
	
	public String getAttributeForImage(ImageAttribute imageAttribute, String uuid);
	
	public Asset assignAssetNameToExistingAsset(String uuid);

	public Asset addAsset(Asset asset);
	
	public boolean imageExists(String uuid);

    public void addImageWithAttributes(Map<String, String> attributes, TypeBasedImage.ImageType imageType);

    public void addAttributesToExistingImageWithId(String imageId, Map<String, String> attributes);
    
    List<Image> retrieveProcessedImages();
    
    List<String> retrieveFormatsForMasterImage(String masterImageUuid);
}
