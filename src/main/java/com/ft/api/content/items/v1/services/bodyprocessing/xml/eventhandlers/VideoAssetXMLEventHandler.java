package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;

public class VideoAssetXMLEventHandler extends AssetXMLEventHandler implements AssetXMLEventHandlable {

	private static final QName VIDEO_TAG_QNAME = QName.valueOf( "videoPlayer");
	private static final String VIDEO_TYPE = "video";
	private static final String VIDEO_ID = "videoID";


	public VideoAssetXMLEventHandler(XMLEventHandler fallbackEventHandler) {
		super(fallbackEventHandler, VIDEO_TAG_QNAME, new VideoAssetManager());
	}

	public VideoAssetXMLEventHandler(XMLEventHandler fallbackEventHandler, AssetManager assetManager) {
		super(fallbackEventHandler, VIDEO_TAG_QNAME, assetManager);
	}

	@Override
	public String getAssetType() {
		return VIDEO_TYPE;
	}

	@Override
	public boolean isAssetLink(StartElement event) {
		return event.getAttributeByName(QName.valueOf(VIDEO_ID)) != null;
	}

	@Override
	protected boolean isInsidePTag() {
		return false;
	}
}
