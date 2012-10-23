package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ft.unifiedContentModel.model.Asset;
import com.ft.unifiedContentModel.model.PromoBox;
import com.ft.unifiedContentModel.model.PromoBoxFields;
import com.ft.unifiedContentModel.model.PromoBoxImage;

public class PromoBoxDataTest {
    private PromoBoxData promoBoxData;
    private String title = "some title";
    private String imageFileRef = "some image file ref";
    private String headline = "some headline";
    private String intro = "some intro";
    private String link = "some link";
    private String imageType = "some image type";
    private String imageHeight = "some image height";
    private String imageWidth = "some image width";
    private String imageAlt = "some image alt";
    private String imageUrl = "http://someimageurl";
    
    @Before
    public void setUp() {
        promoBoxData = new PromoBoxData();
    }
    
    @Test
        public void testIsAllRequiredDataPresentWhenSourceAndTextPresent() {
            promoBoxData.setTitle(title);
            promoBoxData.setImageFileRef(imageFileRef);
            assertTrue(promoBoxData.isAllRequiredDataPresent());
        }
    
    @Test
        public void testIsAllRequiredDataPresentWhenTitlePresent() {
            promoBoxData.setTitle(title);     
            assertFalse(promoBoxData.isAllRequiredDataPresent());
        }
    
    @Test
        public void testIsAllRequiredDataPresentWhenImageFileRefPresent() {
            promoBoxData.setImageFileRef(imageFileRef);
            assertFalse(promoBoxData.isAllRequiredDataPresent());
        }
    
    @Test
        public void testIsAllRequiredDataPresentWhenNoDataPresent() {
            assertFalse(promoBoxData.isAllRequiredDataPresent());
        }
    
    @Test
    public void testGetAssetIsValid() {
        promoBoxData.setTitle(title);
        promoBoxData.setImageFileRef(imageFileRef);
        promoBoxData.setHeadline(headline);
        promoBoxData.setIntro(intro);
        promoBoxData.setLink(link);
        
        promoBoxData.setImageType(imageType);
        promoBoxData.setImageHeight(imageHeight);
        promoBoxData.setImageWidth(imageWidth);
        promoBoxData.setImageAlt(imageAlt);
        promoBoxData.setImageUrl(imageUrl);
        
        Asset actualAsset = promoBoxData.getAsset();
        assertNotNull("The asset should not be null.", actualAsset);
        
        assertTrue("The asset is not of the expected type.", actualAsset instanceof PromoBox);
        PromoBoxFields actualFields = ((PromoBox)actualAsset).getFields();
        assertNotNull("The fields should not be null.", actualFields);
        Assert.assertEquals("The title was not as expected.", actualFields.getTitle(), title);
        Assert.assertEquals("The headline was not as expected.", actualFields.getHeadline(), headline);
        Assert.assertEquals("The headline was not as expected.", actualFields.getIntro(), intro);
        Assert.assertEquals("The headline was not as expected.", actualFields.getLink(), link);
        
        PromoBoxImage actualPromoBoxImage = actualFields.getImage();
        Assert.assertEquals("The image url was not as expected.", actualPromoBoxImage.getUrl(), imageUrl);
        Assert.assertEquals("The image type was not as expected.", actualPromoBoxImage.getType(), imageType);
        Assert.assertEquals("The image height was not as expected.", actualPromoBoxImage.getHeight(), imageHeight);
        Assert.assertEquals("The image width was not as expected.", actualPromoBoxImage.getWidth(), imageWidth);
        Assert.assertEquals("The image alt was not as expected.", actualPromoBoxImage.getAlt(), imageAlt);
    }
    
    @Test
    public void testGetAssetIsValidNoImage() {
        promoBoxData.setTitle(title);
        promoBoxData.setImageFileRef(imageFileRef);
        promoBoxData.setHeadline(headline);
        promoBoxData.setIntro(intro);
        promoBoxData.setLink(link);
       
        Asset actualAsset = promoBoxData.getAsset();
        assertNotNull("The asset should not be null.", actualAsset);
        
        assertTrue("The asset is not of the expected type.", actualAsset instanceof PromoBox);
        PromoBoxFields actualFields = ((PromoBox)actualAsset).getFields();
        assertNotNull("The fields should not be null.", actualFields);
        Assert.assertEquals("The title was not as expected.", actualFields.getTitle(), title);
        Assert.assertEquals("The headline was not as expected.", actualFields.getHeadline(), headline);
        Assert.assertEquals("The headline was not as expected.", actualFields.getIntro(), intro);
        Assert.assertEquals("The headline was not as expected.", actualFields.getLink(), link);
        
        PromoBoxImage actualPromoBoxImage = actualFields.getImage();
        assertNull("The PromoBoxImage should be null.", actualPromoBoxImage);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAssetWhenNotOkToRender() {
        promoBoxData.getAsset();
    }
}
