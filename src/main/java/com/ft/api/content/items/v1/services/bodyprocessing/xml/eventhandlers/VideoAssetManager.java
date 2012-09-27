package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.unifiedContentModel.model.Asset;
import com.ft.unifiedContentModel.model.VideoAsset;
import com.ft.unifiedContentModel.model.VideoFields;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

public class VideoAssetManager extends ExtractableAssetManager {
	private static final String VIDEO_SOURCE = "Brightcove";
	private static final String VIDEO_ID = "videoID";

	@Override
		protected Asset createAsset(StartElement event) {

		Attribute videoId = event.getAttributeByName(QName.valueOf(VIDEO_ID));
		if(videoId == null){
			return null;
		}
		VideoFields fields = new VideoFields(VIDEO_SOURCE, videoId.getValue());
		return new VideoAsset(fields);
	}
}
