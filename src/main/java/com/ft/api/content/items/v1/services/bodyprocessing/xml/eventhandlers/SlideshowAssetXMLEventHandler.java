package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

public class SlideshowAssetXMLEventHandler extends AssetXMLEventHandler{
	
	private static final String TYPE_SLIDESHOW = "slideshow";
	private static final QName A_TAG_QNAME = QName.valueOf("a");
	private static final String TYPE_ATTRIBUTE_NAME = "type";


	public SlideshowAssetXMLEventHandler(XMLEventHandler fallbackEventHandler) {
		super(fallbackEventHandler, A_TAG_QNAME, new ExistingAssetManager());
	}


	public boolean isAssetLink(StartElement event) {
		Attribute typeAttribute = event.getAttributeByName(QName.valueOf(TYPE_ATTRIBUTE_NAME));
		return typeAttribute != null && TYPE_SLIDESHOW.equals(typeAttribute.getValue());
	}

	@Override
	public String getAssetType() {
		return TYPE_SLIDESHOW;
	}
}
