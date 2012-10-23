package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.unifiedContentModel.model.Asset;
import com.ft.unifiedContentModel.model.DataTable;
import com.ft.unifiedContentModel.model.DataTableFields;


public class DataTableDataTest {
    
    private static final String BODY_TEXT = "some body";

    @Test
    public void shouldBeTrueIsOkToRender() {
        DataTableData dataTableData = new DataTableData();
        dataTableData.setBody(BODY_TEXT);
        assertTrue(dataTableData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldNotBeTrueIsOkToRenderWithSpaces() {
        DataTableData dataTableData = new DataTableData();
        dataTableData.setBody("  ");
        assertFalse(dataTableData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldNotBeTrueIsOkToRenderWithEmptyString() {
        DataTableData dataTableData = new DataTableData();
        dataTableData.setBody("");
        assertFalse(dataTableData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldNotBeTrueIsOkToRenderWithNewLine() {
        DataTableData dataTableData = new DataTableData();
        dataTableData.setBody("\n");
        assertFalse(dataTableData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldNotBeTrueIsOkToRenderWithNull() {
        DataTableData dataTableData = new DataTableData();
        dataTableData.setBody(null);
        assertFalse(dataTableData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldReturnAValidAsset() {
        DataTableData dataTableData = new DataTableData();
        dataTableData.setBody(BODY_TEXT);
        
        Asset asset = dataTableData.getAsset();
        assertNotNull(asset);
        assertTrue(asset instanceof DataTable);
        DataTable dataTable = (DataTable)asset;
        DataTableFields fields = dataTable.getFields();
        assertNotNull(fields);
        assertEquals(BODY_TEXT, fields.getBody());
    }
    
    @Test(expected = BodyProcessingException.class)
    public void shouldNotReturnAnAsset() {
        DataTableData dataTableData = new DataTableData();
        dataTableData.setBody("");
        dataTableData.getAsset();
    }
}
