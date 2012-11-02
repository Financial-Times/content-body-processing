package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import org.junit.Assert;
import org.junit.Test;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.VideoAsset;
import com.ft.api.ucm.model.v1.VideoFields;


public class VideoDataTest {

    private static final String VIDEO_ID = "12345";
    private static final String VIDEO_SOURCE = "Brightcove";

    @Test 
    public void testIsAllRequiredDataPresentTrue() {
        VideoData videoData = new VideoData();
        videoData.setId(VIDEO_ID);
        
        Assert.assertTrue(videoData.isAllRequiredDataPresent());
    }
    
    @Test 
    public void testIsAllRequiredDataPresentFalse() {
        VideoData videoData = new VideoData();
        videoData.setId("\n");
        
        Assert.assertFalse(videoData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testGetAssetWithValidData() {
        VideoData videoData = new VideoData();
        videoData.setId(VIDEO_ID);
        
        Asset acutalAsset = videoData.getAsset();
        
        Assert.assertNotNull(acutalAsset);
        Assert.assertTrue(acutalAsset instanceof VideoAsset);
        VideoAsset acutalVideoAsset = (VideoAsset) acutalAsset;
        
        VideoFields fields = acutalVideoAsset.getFields();
        Assert.assertNotNull(fields);
        
        Assert.assertEquals(VIDEO_SOURCE, fields.getSource());
        Assert.assertEquals(VIDEO_ID, fields.getSourceReference());
    }
    
    @Test(expected = BodyProcessingException.class)
    public void testGetAssetWithInvalidData() {
        VideoData videoData = new VideoData();
        videoData.setId("\n");
        
        videoData.getAsset();
    }
}
