package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.ImageAttribute;
import com.ft.content.bodyprocessing.xml.StAXTransformingBodyProcessor;

// TODO: add tests for Promobox with master image
@RunWith(MockitoJUnitRunner.class)
public class PromoBoxXMLParserTest extends BaseXMLParserTest {

    private static final String EXPECTED_PARSED_TITLE = "<p><a href=\"http://www.ft.com/reports/ft-500-2011\" title=\"www.ft.com\">FT 500</a></p>";
    private static final String EXPECTED_TRANSFORMED_TITLE = "transformedTitle";

    private static final String EXPECTED_PARSED_HEADLINE = "<p>someheadline</p>";
    private static final String EXPECTED_TRANSFORMED_HEADLINE = "transformedHeadline";

    private static final String EXPECTED_PARSED_INTRO = "<p>The risers and fallers in our annual list of the world’s biggest companies</p>";
    private static final String EXPECTED_TRANSFORMED_INTRO = "transformedIntro";

    private static final String EXPECTED_PARSED_LINK = "<p>somelink</p>";
    private static final String EXPECTED_TRANSFORMED_LINK = "transformedLink";

    private static final String EXPECTED_PARSED_IMAGE_FILE_REF = "/FT/Graphics/Online/Secondary_%26_Triplet_167x96/2011/06/SEC_ft500.jpg?uuid=432b5632-9e79-11e0-9469-00144feabdc0";
    private static final String EXPECTED_PARSED_IMAGE_FILE_REF_NON_STANDARD_UUID = "/FT/Graphics/Online/Secondary_%26_Triplet_167x96/2011/06/SEC_ft500.jpg?uuid=ABCD_432b5632-9e79-11e0-9469-00144feabdc0";
    private static final String EXPECTED_PARSED_IMAGE_FILE_REF_NON_STANDARD_UUID_WITH_SPACES = "/FT/Graphics/Online/Secondary_%26_Triplet_167x96/2011/06/SEC_ft500.jpg?uuid=ABCD_432b5632-9e79-11e0-9469-00144feabdc0    ";
    private static final String EXPECTED_PARSED_IMAGE_FILE_REF_NO_UUID = "/FT/Graphics/Online/Secondary_%26_Triplet_167x96/2011/06/SEC_ft500.jpg?XXXX";
    
    private static final String EXPECTED_IMAGE_URL = "http://someImageUrl";
    private static final String EXPECTED_IMAGE_TYPE = "promo";
    private static final String EXPECTED_IMAGE_HEIGHT = "100";
    private static final String EXPECTED_IMAGE_WIDTH = "170";
    private static final String EXPECTED_IMAGE_ALT = "someAlt";
    
    private static final String EXPECTED_IMAGE_CAPTION = "someCaption";
    private static final String EXPECTED_IMAGE_SOURCE = "someSource";
    private static final String EXPECTED_IMAGE_MEDIA_TYPE = "somemediaType";

    private XMLEventReader xmlEventReader;
    private String uuid = "432b5632-9e79-11e0-9469-00144feabdc0";
    private String nonStandardUuid = "ABCD_432b5632-9e79-11e0-9469-00144feabdc0";
    private String validXml = "<promo-box align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><promo-title><p><a href=\"http://www.ft.com/reports/ft-500-2011\" title=\"www.ft.com\">FT 500</a></p></promo-title></td></tr><tr><td><promo-headline><p>someheadline</p></promo-headline></td></tr><tr><td><promo-image fileref=\"/FT/Graphics/Online/Secondary_%26_Triplet_167x96/2011/06/SEC_ft500.jpg?uuid=432b5632-9e79-11e0-9469-00144feabdc0\"/></td></tr><tr><td><promo-intro><p>The risers and fallers in our annual list of the world’s biggest companies</p></promo-intro></td></tr><tr><td><promo-link><p>somelink</p></promo-link></td></tr></table></promo-box>";
    private String numbersComponentXml = "<promo-box align=\"left\" class=\"numbers-component\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\">"
            + "<tr><td><promo-headline><p>someheadline</p></promo-headline></td></tr>"
            + "<tr><td><promo-intro><p>The risers and fallers in our annual list of the world’s biggest companies</p></promo-intro></td></tr>"
            + "</table></promo-box>";
    private String validXmlNoFileref = "<promo-box align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><promo-title><p><a href=\"http://www.ft.com/reports/ft-500-2011\" title=\"www.ft.com\">FT 500</a></p></promo-title></td></tr><tr><td><promo-headline><p>someheadline</p></promo-headline></td></tr><tr><td><promo-image /></td></tr><tr><td><promo-intro><p>The risers and fallers in our annual list of the world’s biggest companies</p></promo-intro></td></tr><tr><td><promo-link><p>somelink</p></promo-link></td></tr></table></promo-box>";
    
    private PromoBoxXMLParser promoBoxXMLParser;

    @Mock private StAXTransformingBodyProcessor mockStAXTransformingBodyProcessor;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private PromoBoxData mockPromoBoxData;
    @Mock private PromoboxImageData mockPromoBoxImageData;

    @Before
    public void setUp() {
        promoBoxXMLParser = new PromoBoxXMLParser(mockStAXTransformingBodyProcessor);
        when(mockStAXTransformingBodyProcessor.process(EXPECTED_PARSED_TITLE, mockBodyProcessingContext)).thenReturn(EXPECTED_TRANSFORMED_TITLE);
        when(mockStAXTransformingBodyProcessor.process(EXPECTED_PARSED_HEADLINE, mockBodyProcessingContext)).thenReturn(EXPECTED_TRANSFORMED_HEADLINE);
        when(mockStAXTransformingBodyProcessor.process(EXPECTED_PARSED_INTRO, mockBodyProcessingContext)).thenReturn(EXPECTED_TRANSFORMED_INTRO);
        when(mockStAXTransformingBodyProcessor.process(EXPECTED_PARSED_LINK, mockBodyProcessingContext)).thenReturn(EXPECTED_TRANSFORMED_LINK);

        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.HEIGHT, uuid)).thenReturn(EXPECTED_IMAGE_HEIGHT);
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.WIDTH, uuid)).thenReturn(EXPECTED_IMAGE_WIDTH);
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.ALT, uuid)).thenReturn(EXPECTED_IMAGE_ALT);
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.SRC, uuid)).thenReturn(EXPECTED_IMAGE_URL);
        
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.CAPTION, uuid)).thenReturn(EXPECTED_IMAGE_CAPTION);
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.SOURCE, uuid)).thenReturn(EXPECTED_IMAGE_SOURCE);
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.MEDIA_TYPE, uuid)).thenReturn(EXPECTED_IMAGE_MEDIA_TYPE);
        
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.HEIGHT, nonStandardUuid)).thenReturn(EXPECTED_IMAGE_HEIGHT);
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.WIDTH, nonStandardUuid)).thenReturn(EXPECTED_IMAGE_WIDTH);
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.ALT, nonStandardUuid)).thenReturn(EXPECTED_IMAGE_ALT);
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.SRC, nonStandardUuid)).thenReturn(EXPECTED_IMAGE_URL);
        
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.CAPTION, nonStandardUuid)).thenReturn(EXPECTED_IMAGE_CAPTION);
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.SOURCE, nonStandardUuid)).thenReturn(EXPECTED_IMAGE_SOURCE);
        when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.MEDIA_TYPE, nonStandardUuid)).thenReturn(EXPECTED_IMAGE_MEDIA_TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithStAXTransformingBodyNull() {
        new PromoBoxXMLParser(null);
    }

    @Test
    public void testParseElementData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXml);
        StartElement startElement = getStartElement(xmlEventReader);
        PromoBoxData promoBoxData = promoBoxXMLParser.parseElementData(startElement, xmlEventReader);

        assertNotNull("PromoBoxData should not be null", promoBoxData);
        assertTrue(promoBoxData.isAllRequiredDataPresent());
        assertEquals("Title was not as expected", EXPECTED_PARSED_TITLE, promoBoxData.getTitle());
        assertEquals("Headline was not as expected", EXPECTED_PARSED_HEADLINE, promoBoxData.getHeadline());
        assertEquals("Intro was not as expected", EXPECTED_PARSED_INTRO, promoBoxData.getIntro());
        assertEquals("Link was not as expected", EXPECTED_PARSED_LINK, promoBoxData.getLink());
        String imageFileRef = promoBoxData.getPromoboxImages().get(0).getImageFileRef();
        assertEquals("Image file ref was not as expected", EXPECTED_PARSED_IMAGE_FILE_REF, imageFileRef);
    }
    
    @Test
    public void testTransformFieldContentToStructuredFormatWithNonStandardUuid() {
        when(mockPromoBoxData.getTitle()).thenReturn(EXPECTED_PARSED_TITLE);
        when(mockPromoBoxData.getHeadline()).thenReturn(EXPECTED_PARSED_HEADLINE);
        when(mockPromoBoxData.getIntro()).thenReturn(EXPECTED_PARSED_INTRO);
        when(mockPromoBoxData.getLink()).thenReturn(EXPECTED_PARSED_LINK);
        when(mockPromoBoxData.getPromoboxImages()).thenReturn(Arrays.asList(mockPromoBoxImageData));
        
        when(mockPromoBoxImageData.getImageFileRef()).thenReturn(EXPECTED_PARSED_IMAGE_FILE_REF_NON_STANDARD_UUID);
        when(mockBodyProcessingContext.imageExists(nonStandardUuid)).thenReturn(true);
        
        promoBoxXMLParser.transformFieldContentToStructuredFormat(mockPromoBoxData, mockBodyProcessingContext);

        verifyStAXTransformingBodyProcessor();
        verifyPromoboxImageFields();
           
    }
    
    @Test
    public void testTransformFieldContentToStructuredFormatWithNonStandardUuidWSpaces() {
        when(mockPromoBoxData.getTitle()).thenReturn(EXPECTED_PARSED_TITLE);
        when(mockPromoBoxData.getHeadline()).thenReturn(EXPECTED_PARSED_HEADLINE);
        when(mockPromoBoxData.getIntro()).thenReturn(EXPECTED_PARSED_INTRO);
        when(mockPromoBoxData.getLink()).thenReturn(EXPECTED_PARSED_LINK);
        when(mockPromoBoxData.getPromoboxImages()).thenReturn(Arrays.asList(mockPromoBoxImageData));
        
        when(mockPromoBoxImageData.getImageFileRef()).thenReturn(EXPECTED_PARSED_IMAGE_FILE_REF_NON_STANDARD_UUID_WITH_SPACES);
        when(mockBodyProcessingContext.imageExists(nonStandardUuid)).thenReturn(true);
        
        promoBoxXMLParser.transformFieldContentToStructuredFormat(mockPromoBoxData, mockBodyProcessingContext);

        verifyStAXTransformingBodyProcessor();
        verifyPromoboxImageFields();
        
    }

    private void verifyStAXTransformingBodyProcessor() {
        verify(mockStAXTransformingBodyProcessor).process(EXPECTED_PARSED_TITLE, mockBodyProcessingContext);
        verify(mockStAXTransformingBodyProcessor).process(EXPECTED_PARSED_HEADLINE, mockBodyProcessingContext);
        verify(mockStAXTransformingBodyProcessor).process(Matchers.eq(EXPECTED_PARSED_INTRO), Matchers.eq(mockBodyProcessingContext));
        verify(mockStAXTransformingBodyProcessor).process(Matchers.eq(EXPECTED_PARSED_LINK), Matchers.eq(mockBodyProcessingContext));
    }

    @Test
    public void testTransformFieldContentToStructuredFormatWithNonStandardUuidWithSpaces() {
        when(mockPromoBoxData.getTitle()).thenReturn(EXPECTED_PARSED_TITLE);
        when(mockPromoBoxData.getHeadline()).thenReturn(EXPECTED_PARSED_HEADLINE);
        when(mockPromoBoxData.getIntro()).thenReturn(EXPECTED_PARSED_INTRO);
        when(mockPromoBoxData.getLink()).thenReturn(EXPECTED_PARSED_LINK);
        when(mockPromoBoxData.getPromoboxImages()).thenReturn(Arrays.asList(mockPromoBoxImageData));
        
        when(mockPromoBoxImageData.getImageFileRef()).thenReturn(EXPECTED_PARSED_IMAGE_FILE_REF_NON_STANDARD_UUID);
        when(mockBodyProcessingContext.imageExists(nonStandardUuid)).thenReturn(true);
        
        promoBoxXMLParser.transformFieldContentToStructuredFormat(mockPromoBoxData, mockBodyProcessingContext);

        verifyStAXTransformingBodyProcessor();
        verifyPromoboxImageFields();
    }
    
    @Test
    public void testTransformFieldContentToStructuredFormat() {
        when(mockPromoBoxData.getTitle()).thenReturn(EXPECTED_PARSED_TITLE);
        when(mockPromoBoxData.getHeadline()).thenReturn(EXPECTED_PARSED_HEADLINE);
        when(mockPromoBoxData.getIntro()).thenReturn(EXPECTED_PARSED_INTRO);
        when(mockPromoBoxData.getLink()).thenReturn(EXPECTED_PARSED_LINK);
        when(mockPromoBoxData.getPromoboxImages()).thenReturn(Arrays.asList(mockPromoBoxImageData));
        
        when(mockPromoBoxImageData.getImageFileRef()).thenReturn(EXPECTED_PARSED_IMAGE_FILE_REF);
        when(mockBodyProcessingContext.imageExists(uuid)).thenReturn(true);
        
        promoBoxXMLParser.transformFieldContentToStructuredFormat(mockPromoBoxData, mockBodyProcessingContext);

        verifyStAXTransformingBodyProcessor();
        verifyPromoboxImageFields();
    }
    
    @Test
    public void testTransformFieldContentToStructuredFormatWithBlankContent() {
        when(mockPromoBoxData.getTitle()).thenReturn("\n");
        when(mockPromoBoxData.getHeadline()).thenReturn(" ");
        when(mockPromoBoxData.getIntro()).thenReturn(EXPECTED_PARSED_INTRO);
        when(mockPromoBoxData.getLink()).thenReturn(EXPECTED_PARSED_LINK);
        when(mockPromoBoxData.getPromoboxImages()).thenReturn(Arrays.asList(mockPromoBoxImageData));
        
        when(mockPromoBoxImageData.getImageFileRef()).thenReturn(EXPECTED_PARSED_IMAGE_FILE_REF);
        when(mockBodyProcessingContext.imageExists(uuid)).thenReturn(true);
        
        promoBoxXMLParser.transformFieldContentToStructuredFormat(mockPromoBoxData, mockBodyProcessingContext);

        verify(mockStAXTransformingBodyProcessor, never()).process("\n", mockBodyProcessingContext);
        verify(mockStAXTransformingBodyProcessor, never()).process(" ", mockBodyProcessingContext);
        verify(mockStAXTransformingBodyProcessor).process(Matchers.eq(EXPECTED_PARSED_INTRO), Matchers.eq(mockBodyProcessingContext));
        verify(mockStAXTransformingBodyProcessor).process(Matchers.eq(EXPECTED_PARSED_LINK), Matchers.eq(mockBodyProcessingContext));
        verifyPromoboxImageFields();
    }
    
    @Test
    public void testTransformFieldContentToStructuredFormatNoImageUuid() {
        when(mockPromoBoxData.getTitle()).thenReturn(EXPECTED_PARSED_TITLE);
        when(mockPromoBoxData.getHeadline()).thenReturn(EXPECTED_PARSED_HEADLINE);
        when(mockPromoBoxData.getIntro()).thenReturn(EXPECTED_PARSED_INTRO);
        when(mockPromoBoxData.getLink()).thenReturn(EXPECTED_PARSED_LINK);
        when(mockPromoBoxData.getPromoboxImages()).thenReturn(Arrays.asList(mockPromoBoxImageData));
        
        when(mockPromoBoxImageData.getImageFileRef()).thenReturn(EXPECTED_PARSED_IMAGE_FILE_REF_NO_UUID);

        promoBoxXMLParser.transformFieldContentToStructuredFormat(mockPromoBoxData, mockBodyProcessingContext);

        verifyStAXTransformingBodyProcessor();

        verify(mockPromoBoxImageData, never()).setImageType(EXPECTED_IMAGE_TYPE);
        verify(mockPromoBoxImageData, never()).setImageHeight(EXPECTED_IMAGE_HEIGHT);
        verify(mockPromoBoxImageData, never()).setImageWidth(EXPECTED_IMAGE_WIDTH);
        verify(mockPromoBoxImageData, never()).setImageAlt(EXPECTED_IMAGE_ALT);
    }
    
    @Test
    public void testTransformFieldContentToStructuredFormatUUIDButNoImage() {
        when(mockPromoBoxData.getTitle()).thenReturn(EXPECTED_PARSED_TITLE);
        when(mockPromoBoxData.getHeadline()).thenReturn(EXPECTED_PARSED_HEADLINE);
        when(mockPromoBoxData.getIntro()).thenReturn(EXPECTED_PARSED_INTRO);
        when(mockPromoBoxData.getLink()).thenReturn(EXPECTED_PARSED_LINK);
        when(mockPromoBoxData.getPromoboxImages()).thenReturn(Arrays.asList(mockPromoBoxImageData));
        
        when(mockPromoBoxImageData.getImageFileRef()).thenReturn(EXPECTED_PARSED_IMAGE_FILE_REF);
        when(mockBodyProcessingContext.imageExists(uuid)).thenReturn(false);
                
        promoBoxXMLParser.transformFieldContentToStructuredFormat(mockPromoBoxData, mockBodyProcessingContext);

        verifyStAXTransformingBodyProcessor();

        verify(mockPromoBoxImageData, never()).setImageType(EXPECTED_IMAGE_TYPE);
        verify(mockPromoBoxImageData, never()).setImageHeight(EXPECTED_IMAGE_HEIGHT);
        verify(mockPromoBoxImageData, never()).setImageWidth(EXPECTED_IMAGE_WIDTH);
        verify(mockPromoBoxImageData, never()).setImageAlt(EXPECTED_IMAGE_ALT);
    }
    
    @Test
    public void testTransformFieldContentToStructuredFormatFileRefMissing() {
        when(mockPromoBoxData.getTitle()).thenReturn(EXPECTED_PARSED_TITLE);
        when(mockPromoBoxData.getHeadline()).thenReturn(EXPECTED_PARSED_HEADLINE);
        when(mockPromoBoxData.getIntro()).thenReturn(EXPECTED_PARSED_INTRO);
        when(mockPromoBoxData.getLink()).thenReturn(EXPECTED_PARSED_LINK);
        when(mockPromoBoxData.getPromoboxImages()).thenReturn(Arrays.asList(mockPromoBoxImageData));
        
        when(mockPromoBoxImageData.getImageFileRef()).thenReturn(null);
        when(mockBodyProcessingContext.imageExists(uuid)).thenReturn(false);
                
        promoBoxXMLParser.transformFieldContentToStructuredFormat(mockPromoBoxData, mockBodyProcessingContext);

        verifyStAXTransformingBodyProcessor();

        verify(mockPromoBoxImageData, never()).setImageType(EXPECTED_IMAGE_TYPE);
        verify(mockPromoBoxImageData, never()).setImageHeight(EXPECTED_IMAGE_HEIGHT);
        verify(mockPromoBoxImageData, never()).setImageWidth(EXPECTED_IMAGE_WIDTH);
        verify(mockPromoBoxImageData, never()).setImageAlt(EXPECTED_IMAGE_ALT);
    }
    
    @Test
    public void testParseElementDataWithValidXmlNoFileref() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXmlNoFileref);
        StartElement startElement = getStartElement(xmlEventReader);
        PromoBoxData promoBoxData = promoBoxXMLParser.parseElementData(startElement, xmlEventReader);
        assertTrue(promoBoxData.isAllRequiredDataPresent());
    }
    
    @Test
    public void testNumbersComponent() throws Exception {
        xmlEventReader = createReaderForXml(numbersComponentXml);
        StartElement startElement = getStartElement(xmlEventReader);
        PromoBoxData promoBoxData = promoBoxXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertTrue(promoBoxData.isAllRequiredDataPresent());
        assertTrue(promoBoxData.isNumbersComponent());
        
        assertEquals("Headline was not as expected.", EXPECTED_PARSED_HEADLINE, promoBoxData.getHeadline());
        assertEquals("Intro was not as expected.", EXPECTED_PARSED_INTRO, promoBoxData.getIntro());
    }
    
    private void verifyPromoboxImageFields() {
        verify(mockPromoBoxImageData).setImageType(EXPECTED_IMAGE_TYPE);
        verify(mockPromoBoxImageData).setImageHeight(EXPECTED_IMAGE_HEIGHT);
        verify(mockPromoBoxImageData).setImageWidth(EXPECTED_IMAGE_WIDTH);
        verify(mockPromoBoxImageData).setImageAlt(EXPECTED_IMAGE_ALT);

        verify(mockPromoBoxImageData).setImageCaption(EXPECTED_IMAGE_CAPTION);
        verify(mockPromoBoxImageData).setImageSource(EXPECTED_IMAGE_SOURCE);
        verify(mockPromoBoxImageData).setImageMediaType(EXPECTED_IMAGE_MEDIA_TYPE);
    }
}
