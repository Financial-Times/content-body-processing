package com.ft.api.content.items.v1.services.bodyprocessing;


import com.ft.unifiedContentModel.model.Asset;

public interface BodyProcessingContext {

	public String getAttributeForImage(String attributeName, String uuid);
	
	public Asset assignAssetNameToExistingAsset(String uuid);

	public Asset addAsset(Asset asset);
	
	public boolean imageExists(String uuid);

}
