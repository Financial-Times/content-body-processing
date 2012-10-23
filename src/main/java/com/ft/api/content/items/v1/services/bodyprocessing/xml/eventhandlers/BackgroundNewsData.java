package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.unifiedContentModel.model.Asset;
import com.ft.unifiedContentModel.model.BackgroundNews;
import com.ft.unifiedContentModel.model.BackgroundNewsFields;
import org.apache.commons.lang.StringUtils;

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
		return !StringUtils.isEmpty(this.text) || !StringUtils.isEmpty(this.header);
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
