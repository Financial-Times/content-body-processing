package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;


import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.unifiedContentModel.model.Asset;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import org.apache.commons.lang.StringUtils;

public class ExistingAssetManager implements AssetManager {
	private static final String UUID_KEY = "uuid";
	private static final QName HREF_QNAME = QName.valueOf("href");


	private String extractUuidFromHref(StartElement element){
		Attribute hrefElement = element.getAttributeByName(HREF_QNAME);
		if(hrefElement != null) {
			String[] attributesSides = StringUtils.splitPreserveAllTokens(hrefElement.getValue(), "?");
			if(attributesSides.length == 2) {
				String[] attributes = StringUtils.splitPreserveAllTokens(attributesSides[1], "&");
				for(String attribute: attributes){
					String[] keyValue = StringUtils.splitPreserveAllTokens(attribute, "=");
					if(UUID_KEY.equalsIgnoreCase(keyValue[0])){
						if(keyValue.length == 2){
							return keyValue[1];
						}
					}
				}
			}
		}
		return null;
	}

	public Asset createValidAttributesForMediaAsset(StartElement event, BodyProcessingContext bodyProcessingContext, String assetType){
		String uuid = extractUuidFromHref(event);
		if(StringUtils.isBlank(uuid)){
			return null;
		}
		return bodyProcessingContext.assignAssetNameToExistingAsset(uuid);
	}
}
