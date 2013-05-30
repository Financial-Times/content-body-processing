package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.DataTable;
import com.ft.api.ucm.model.v1.DataTableFields;

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
		return containsValidData(this.body);
	}

	@Override
	public Asset getAsset() throws BodyProcessingException {
		DataTable webTable = null;
		if(isAllRequiredDataPresent()){
			webTable = new DataTable();
			DataTableFields dataTableFields = new DataTableFields(nullIfEmpty(this.getBody()));
			webTable.setFields(dataTableFields);
			return webTable;
		}
		throw new BodyProcessingException(GET_ASSET_NO_VALID_EXCEPTION_MESSAGE);

	}
}
