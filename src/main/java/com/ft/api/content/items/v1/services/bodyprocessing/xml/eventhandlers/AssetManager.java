package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;


import javax.xml.stream.events.StartElement;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.ucm.model.v1.Asset;

public interface AssetManager {
	Asset createValidAttributesForMediaAsset(StartElement event, BodyProcessingContext bodyProcessingContext, String assetType);
}
