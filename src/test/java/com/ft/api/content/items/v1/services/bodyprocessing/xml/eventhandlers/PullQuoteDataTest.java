package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ft.unifiedContentModel.model.Asset;
import com.ft.unifiedContentModel.model.PullQuote;
import com.ft.unifiedContentModel.model.PullQuoteFields;


public class PullQuoteDataTest {
    
    private PullQuoteData pullQuoteData;
    private String quoteSource = "some source";
    private String quoteText = "some text";
    
    @Before
    public void setUp() {
        pullQuoteData = new PullQuoteData();
    }
    
    @Test
    public void testIsOkToRenderWhenSourceAndTextPresent() {
        pullQuoteData.setQuoteSource(quoteSource);
        pullQuoteData.setQuoteText(quoteText);
        assertTrue(pullQuoteData.isOkToRender());
    }
    
    @Test
    public void testIsOkToRenderWhenSourcePresent() {
        pullQuoteData.setQuoteSource(quoteSource);        
        assertTrue(pullQuoteData.isOkToRender());
    }
    
    @Test
    public void testIsOkToRenderWhenTextPresent() {
        pullQuoteData.setQuoteText(quoteText);
        assertTrue(pullQuoteData.isOkToRender());
    }
    
    @Test
    public void testIsOkToRenderWhenNoDataPresent() {
        assertFalse(pullQuoteData.isOkToRender());
    }
    
    @Test
    public void testIsOkToRenderWhenNewLineDataIsOnlyPresent() {
        pullQuoteData.setQuoteSource("\n");
        pullQuoteData.setQuoteText("\n");
        assertFalse(pullQuoteData.isOkToRender());
    }
    
    @Test
    public void testGetAssetIsValid() {
        pullQuoteData.setQuoteSource(quoteSource);
        pullQuoteData.setQuoteText(quoteText);
        Asset actualAsset = pullQuoteData.getAsset();
        assertNotNull("The asset should not be null.", actualAsset);
        
        assertTrue("The asset is not of the expected type.", actualAsset instanceof PullQuote);
        PullQuoteFields actualFields = ((PullQuote)actualAsset).getFields();
        assertNotNull("The fields should not be null.", actualFields);
        Assert.assertEquals("The attribution was not as expected.", actualFields.getAttribution(), quoteSource);
        Assert.assertEquals("The body was not as expected.", actualFields.getBody(), quoteText);
    }
    
    @Test
    public void testGetAssetIsValidNoTrailingSpaces() {
        pullQuoteData.setQuoteSource(quoteSource.concat(" "));
        pullQuoteData.setQuoteText(quoteText.concat("      "));
        Asset actualAsset = pullQuoteData.getAsset();
        assertNotNull("The asset should not be null.", actualAsset);
        
        assertTrue("The asset is not of the expected type.", actualAsset instanceof PullQuote);
        PullQuoteFields actualFields = ((PullQuote)actualAsset).getFields();
        assertNotNull("The fields should not be null.", actualFields);
        Assert.assertEquals("The attribution was not as expected.", actualFields.getAttribution(), quoteSource);
        Assert.assertEquals("The body was not as expected.", actualFields.getBody(), quoteText);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetAssetWhenNotOkToRender() {
        pullQuoteData.getAsset();
    }
    
    @Test
    public void testEmptyWSpacesSourceFieldIsNull() {
        pullQuoteData.setQuoteSource("sometext");
        pullQuoteData.setQuoteText(" ");
        Asset actualAsset = pullQuoteData.getAsset();
        assertNotNull("The asset should not be null.", actualAsset);
        
        PullQuoteFields actualFields = ((PullQuote)actualAsset).getFields();
        assertNotNull("The fields should not be null.", actualFields);
        assertNotNull("The attribution was not as expected.", actualFields.getAttribution());
        assertNull("The body was not as expected.", actualFields.getBody());
    }
    
    @Test
    public void testEmptySourceFieldIsNull() {
        pullQuoteData.setQuoteSource("sometext");
        pullQuoteData.setQuoteText("");
        Asset actualAsset = pullQuoteData.getAsset();
        assertNotNull("The asset should not be null.", actualAsset);
        
        PullQuoteFields actualFields = ((PullQuote)actualAsset).getFields();
        assertNotNull("The fields should not be null.", actualFields);
        assertNotNull("The attribution was not as expected.", actualFields.getAttribution());
        assertNull("The body was not as expected.", actualFields.getBody());
    }
    
    @Test
    public void testEmptyWSpacesTextFieldIsNull() {
        pullQuoteData.setQuoteSource(" ");
        pullQuoteData.setQuoteText("sometext");
        Asset actualAsset = pullQuoteData.getAsset();
        assertNotNull("The asset should not be null.", actualAsset);
        
        PullQuoteFields actualFields = ((PullQuote)actualAsset).getFields();
        assertNotNull("The fields should not be null.", actualFields);
        assertNull("The attribution was not as expected.", actualFields.getAttribution());
        assertNotNull("The body was not as expected.", actualFields.getBody());
    }
    
    @Test
    public void testEmptyTextFieldIsNull() {
        pullQuoteData.setQuoteSource("");
        pullQuoteData.setQuoteText("sometext");
        Asset actualAsset = pullQuoteData.getAsset();
        assertNotNull("The asset should not be null.", actualAsset);
        
        PullQuoteFields actualFields = ((PullQuote)actualAsset).getFields();
        assertNotNull("The fields should not be null.", actualFields);
        assertNull("The attribution was not as expected.", actualFields.getAttribution());
        assertNotNull("The body was not as expected.", actualFields.getBody());
    }
}
