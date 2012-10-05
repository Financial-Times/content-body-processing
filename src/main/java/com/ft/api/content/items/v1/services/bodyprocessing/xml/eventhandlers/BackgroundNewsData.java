package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

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
	public boolean isOkToRender() {
		return !StringUtils.isEmpty(this.text) || !StringUtils.isEmpty(this.header);
	}

	@Override
	public Asset getAsset() throws IllegalStateException {
		BackgroundNews backgroundNews = null;
		if(isOkToRender()){
			backgroundNews = new BackgroundNews();
			BackgroundNewsFields backgroundNewsFields = new BackgroundNewsFields(nullIfEmpty(this.getHeader()), nullIfEmpty(this.getText()));
			backgroundNews.setFields(backgroundNewsFields);
			return backgroundNews;
		}
		throw new IllegalStateException("The object does not have sufficient data to render a valid asset. Only if the method isOkToRender is true will this method return a valid asset.");
	}
}
