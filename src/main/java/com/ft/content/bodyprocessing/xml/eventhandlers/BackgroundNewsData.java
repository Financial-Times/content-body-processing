package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.BackgroundNews;
import com.ft.api.ucm.model.v1.BackgroundNewsFields;

public class BackgroundNewsData extends BaseData implements AssetAware {

	private String text;
	private String header;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	@Override
	public boolean isAllRequiredDataPresent() {
		return containsValidData(this.text) || containsValidData(this.header);
	}

	@Override
	public Asset getAsset() throws BodyProcessingException {
		BackgroundNews backgroundNews = null;
		if(isAllRequiredDataPresent()){
			backgroundNews = new BackgroundNews();
			BackgroundNewsFields backgroundNewsFields = new BackgroundNewsFields(nullIfEmpty(this.getHeader()), nullIfEmpty(this.getText()));
			backgroundNews.setFields(backgroundNewsFields);
			return backgroundNews;
		}
		throw new BodyProcessingException(GET_ASSET_NO_VALID_EXCEPTION_MESSAGE);
	}
}
