package com.ft.api.content.items.v1.services.bodyprocessing;

import com.ft.api.ucm.model.v1.Asset;



public interface BodyProcessingContext {

    @Deprecated
	public String getAttributeForImage(String attributeName, String uuid);
	
	public String getAttributeForImage(ImageAttribute imageAttribute, String uuid);
	
	public Asset assignAssetNameToExistingAsset(String uuid);

	public Asset addAsset(Asset asset);
	
	public boolean imageExists(String uuid);

}
