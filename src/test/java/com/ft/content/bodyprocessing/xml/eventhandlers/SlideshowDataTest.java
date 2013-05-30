package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class SlideshowDataTest {

    @Test
    public void testIsAllRequiredDataPresentWhenTrue() {
        SlideshowData slideshowData = new SlideshowData();
        slideshowData.setUuid("someUuid");
        
        assertTrue(slideshowData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenFalse() {
        SlideshowData slideshowData = new SlideshowData();
        slideshowData.setUuid("\n");
        
        assertFalse(slideshowData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testGetAssetShouldReturnNull() {
        SlideshowData slideshowData = new SlideshowData();
        assertNull(slideshowData.getAsset());
    }
}
