package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import javax.xml.stream.events.StartElement;

public interface AssetXMLEventHandlable {

	boolean isAssetLink(StartElement event);
	boolean isValidElement(StartElement event);

	String getAssetType();
}
