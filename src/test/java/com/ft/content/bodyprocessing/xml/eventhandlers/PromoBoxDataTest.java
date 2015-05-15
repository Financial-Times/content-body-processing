package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.PromoBox;
import com.ft.api.ucm.model.v1.PromoBoxFields;
import com.ft.api.ucm.model.v1.TypeBasedImage;

public class PromoBoxDataTest {
    private PromoBoxData promoBoxData;
    private String title = "some title";
    private String imageFileRef = "some image file ref";
    private String headline = "some headline";
    private String intro = "some intro";
    private String link = "some link";
    private String imageType = "promo";
    private String imageHeight = "10";
    private String imageWidth = "20";
    private String imageAlt = "some image alt";
    private String imageUrl = "http://someimageurl";
    
    private String imageSource = "some image source";
    private String imageCaption = "some image caption";
    private String imageMediaType = "some image media type";
    
    @Before
    public void setUp() {
        promoBoxData = new PromoBoxData();
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenImageFileRefPresent() {
        promoBoxData.addImageToPromobox(imageFileRef);
        assertTrue(promoBoxData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenHeadlinePresent() {
        promoBoxData.setHeadline(headline);
        assertTrue(promoBoxData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenIntroPresent() {
        promoBoxData.setIntro(intro);
        assertTrue(promoBoxData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenLinkPresent() {
        promoBoxData.setLink(link);
        assertTrue(promoBoxData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenTitlePresent() {
        promoBoxData.setTitle(title);
        assertTrue(promoBoxData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenImageFileRefIsNewLineData() {
        promoBoxData.addImageToPromobox("\n");   
        assertFalse(promoBoxData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenTitleIsNewLineData() {
        promoBoxData.setTitle("\n"); 
        assertFalse(promoBoxData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenNoDataPresent() {
        assertFalse(promoBoxData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testIsAllRequiredDataPresentWhenNoDataPresentBothNewLineData() {
        promoBoxData.addImageToPromobox("\n"); 
        promoBoxData.setTitle("\n"); 
        assertFalse(promoBoxData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testGetAssetIsValid() {
        PromoboxImageData imageData = promoBoxData.addImageToPromobox(imageFileRef);
        promoBoxData.setTitle(title);
        promoBoxData.setHeadline(headline);
        promoBoxData.setIntro(intro);
        promoBoxData.setLink(link);
        
        imageData.setImageType(imageType);
        imageData.setImageHeight(imageHeight);
        imageData.setImageWidth(imageWidth);
        imageData.setImageAlt(imageAlt);
        imageData.setImageUrl(imageUrl);
        
        imageData.setImageSource(imageSource);
        imageData.setImageCaption(imageCaption);
        imageData.setImageMediaType(imageMediaType);
        
        Asset actualAsset = promoBoxData.getAsset();
        assertNotNull("The asset should not be null.", actualAsset);
        
        assertTrue("The asset is not of the expected type.", actualAsset instanceof PromoBox);
        PromoBoxFields actualFields = ((PromoBox)actualAsset).getFields();
        assertNotNull("The fields should not be null.", actualFields);
        Assert.assertEquals("The title was not as expected.", actualFields.getTitle(), title);
        Assert.assertEquals("The headline was not as expected.", actualFields.getHeadline(), headline);
        Assert.assertEquals("The headline was not as expected.", actualFields.getIntro(), intro);
        Assert.assertEquals("The headline was not as expected.", actualFields.getLink(), link);
        
        TypeBasedImage actualPromoBoxImage = actualFields.getImages().get(0);
        Assert.assertEquals("The image url was not as expected.", actualPromoBoxImage.getUrl(), imageUrl);
        Assert.assertEquals("The image type was not as expected.", actualPromoBoxImage.getType(), imageType);
        Assert.assertEquals("The image height was not as expected.", actualPromoBoxImage.getHeight(), new Integer(imageHeight));
        Assert.assertEquals("The image width was not as expected.", actualPromoBoxImage.getWidth(), new Integer(imageWidth));
        Assert.assertEquals("The image alt was not as expected.", actualPromoBoxImage.getAlt(), imageAlt);
        
        Assert.assertEquals("The image caption was not as expected.", actualPromoBoxImage.getCaption(), imageCaption);
        Assert.assertEquals("The image source was not as expected.", actualPromoBoxImage.getSource(), imageSource);
        Assert.assertEquals("The image mediaType was not as expected.", actualPromoBoxImage.getMediaType(), imageMediaType);
    }
    
    @Test
    public void testGetAssetIsValidNoImage() {
        promoBoxData.addImageToPromobox(imageFileRef);
        promoBoxData.setTitle(title);
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
        
        assertNull("The PromoBoxImage should be null.", actualFields.getImages());
    }

    @Test(expected = BodyProcessingException.class)
    public void testGetAssetWhenNotOkToRender() {
        promoBoxData.getAsset();
    }
}
