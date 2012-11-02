package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.BackgroundNews;
import com.ft.api.ucm.model.v1.BackgroundNewsFields;


public class BackgroundNewsDataTest {
    
    private static final String HEADER = "some header";
    private static final String TEXT = "some text";
    
    @Test
    public void shouldBeTrueIsOkToRender() {
        BackgroundNewsData backgroundNewsData = new BackgroundNewsData();
        backgroundNewsData.setHeader(HEADER);
        backgroundNewsData.setText(TEXT);
        assertTrue(backgroundNewsData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldBeTrueIsAllRequiredDataPresentNoHeader() {
        BackgroundNewsData backgroundNewsData = new BackgroundNewsData();
        backgroundNewsData.setHeader("");
        backgroundNewsData.setText(TEXT);
        assertTrue(backgroundNewsData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldBeTrueIsAllRequiredDataPresentNoText() {
        BackgroundNewsData backgroundNewsData = new BackgroundNewsData();
        backgroundNewsData.setHeader(HEADER);
        backgroundNewsData.setText("");
        assertTrue(backgroundNewsData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldBeTrueIsAllRequiredDataPresentNoHeaderSpaces() {
        BackgroundNewsData backgroundNewsData = new BackgroundNewsData();
        backgroundNewsData.setHeader("  ");
        backgroundNewsData.setText(TEXT);
        assertTrue(backgroundNewsData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldBeTrueIsAllRequiredDataPresentNoTextSpaces() {
        BackgroundNewsData backgroundNewsData = new BackgroundNewsData();
        backgroundNewsData.setHeader(HEADER);
        backgroundNewsData.setText("  ");
        assertTrue(backgroundNewsData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldBeTrueIsAllRequiredDataPresentWithNewLineData() {
        BackgroundNewsData backgroundNewsData = new BackgroundNewsData();
        backgroundNewsData.setHeader("\n");
        backgroundNewsData.setText("\n");
        assertFalse(backgroundNewsData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldBeTrueIsAllRequiredDataPresentWithHeaderNewLineData() {
        BackgroundNewsData backgroundNewsData = new BackgroundNewsData();
        backgroundNewsData.setHeader("\n");
        backgroundNewsData.setText(TEXT);
        assertTrue(backgroundNewsData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldBeTrueIsAllRequiredDataPresentWithTextNewLineData() {
        BackgroundNewsData backgroundNewsData = new BackgroundNewsData();
        backgroundNewsData.setHeader(HEADER);
        backgroundNewsData.setText("\n");
        assertTrue(backgroundNewsData.isAllRequiredDataPresent());
    }
    
    @Test
    public void shouldReturnAValidAsset() {
        BackgroundNewsData backgroundNewsData = new BackgroundNewsData();
        backgroundNewsData.setHeader(HEADER);
        backgroundNewsData.setText(TEXT);
        assertTrue(backgroundNewsData.isAllRequiredDataPresent());
        
        Asset asset = backgroundNewsData.getAsset();
        assertNotNull(asset);
        assertTrue(asset instanceof BackgroundNews);
        BackgroundNews backgroundNews = (BackgroundNews)asset;
        BackgroundNewsFields fields = backgroundNews.getFields();
        assertNotNull(fields);
        assertEquals(TEXT, fields.getBody());
        assertEquals(HEADER, fields.getTitle());
    }
    
    @Test
    public void shouldReturnAValidAssetEmptyHeader() {
        BackgroundNewsData backgroundNewsData = new BackgroundNewsData();
        backgroundNewsData.setHeader("\n");
        backgroundNewsData.setText(TEXT);
        assertTrue(backgroundNewsData.isAllRequiredDataPresent());
        
        Asset asset = backgroundNewsData.getAsset();
        assertNotNull(asset);
        assertTrue(asset instanceof BackgroundNews);
        BackgroundNews backgroundNews = (BackgroundNews)asset;
        BackgroundNewsFields fields = backgroundNews.getFields();
        assertNotNull(fields);
        assertEquals(TEXT, fields.getBody());
        assertNull(fields.getTitle());
    }
    
    @Test
    public void shouldReturnAValidAssetEmptyText() {
        BackgroundNewsData backgroundNewsData = new BackgroundNewsData();
        backgroundNewsData.setHeader(HEADER);
        backgroundNewsData.setText("\n");
        assertTrue(backgroundNewsData.isAllRequiredDataPresent());
        
        Asset asset = backgroundNewsData.getAsset();
        assertNotNull(asset);
        assertTrue(asset instanceof BackgroundNews);
        BackgroundNews backgroundNews = (BackgroundNews)asset;
        BackgroundNewsFields fields = backgroundNews.getFields();
        assertNotNull(fields);
        assertNull(fields.getBody());
        assertEquals(HEADER, fields.getTitle());
    }
    
    @Test(expected = BodyProcessingException.class)
    public void shouldNotReturnAnAsset() {
        BackgroundNewsData backgroundNewsData = new BackgroundNewsData();
        backgroundNewsData.getAsset();
    }
}
