package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.unifiedContentModel.model.Asset;
import com.ft.unifiedContentModel.model.DataTable;
import com.ft.unifiedContentModel.model.DataTableFields;
import org.apache.commons.lang.StringUtils;

public class DataTableData extends BaseData implements AssetAware {


	private String body;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public boolean isAllRequiredDataPresent() {
		return !StringUtils.isBlank(this.body);
	}

	@Override
	public Asset getAsset() throws IllegalStateException {
		DataTable webTable = null;
		if(isAllRequiredDataPresent()){
			webTable = new DataTable();
			DataTableFields dataTableFields = new DataTableFields(nullIfEmpty(this.getBody()));
			webTable.setFields(dataTableFields);
			return webTable;
		}
		throw new IllegalStateException("The object does not have sufficient data to render a valid asset. Only if the method isOkToRender is true will this method return a valid asset.");

	}
}
