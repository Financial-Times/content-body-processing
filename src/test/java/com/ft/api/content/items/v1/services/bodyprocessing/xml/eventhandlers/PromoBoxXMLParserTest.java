package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.StAXTransformingBodyProcessor;

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
    private static final String EXPECTED_IMAGE_URL = "http://someImageUrl";
    private static final String EXPECTED_IMAGE_TYPE = "promo";
    private static final String EXPECTED_IMAGE_HEIGHT = "100";
    private static final String EXPECTED_IMAGE_WIDTH = "170";
    private static final String EXPECTED_IMAGE_ALT = "someAlt";

    private XMLEventReader xmlEventReader;
    private String uuid = "432b5632-9e79-11e0-9469-00144feabdc0";
    private String validXml = "<promo-box align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><promo-title><p><a href=\"http://www.ft.com/reports/ft-500-2011\" title=\"www.ft.com\">FT 500</a></p></promo-title></td></tr><tr><td><promo-headline><p>someheadline</p></promo-headline></td></tr><tr><td><promo-image fileref=\"/FT/Graphics/Online/Secondary_%26_Triplet_167x96/2011/06/SEC_ft500.jpg?uuid=432b5632-9e79-11e0-9469-00144feabdc0\"/></td></tr><tr><td><promo-intro><p>The risers and fallers in our annual list of the world’s biggest companies</p></promo-intro></td></tr><tr><td><promo-link><p>somelink</p></promo-link></td></tr></table></promo-box>";

    private PromoBoxXMLParser promoBoxXMLParser;

    @Mock
    private StAXTransformingBodyProcessor mockStAXTransformingBodyProcessor;
    @Mock
    private BodyProcessingContext mockBodyProcessingContext;
    @Mock
    private PromoBoxData mockPromoBoxData;

    @Before
    public void setUp() {
        promoBoxXMLParser = new PromoBoxXMLParser(mockStAXTransformingBodyProcessor);
        when(mockStAXTransformingBodyProcessor.process(EXPECTED_PARSED_TITLE, mockBodyProcessingContext)).thenReturn(EXPECTED_TRANSFORMED_TITLE);
        when(mockStAXTransformingBodyProcessor.process(EXPECTED_PARSED_HEADLINE, mockBodyProcessingContext)).thenReturn(EXPECTED_TRANSFORMED_HEADLINE);
        when(mockStAXTransformingBodyProcessor.process(EXPECTED_PARSED_INTRO, mockBodyProcessingContext)).thenReturn(EXPECTED_TRANSFORMED_INTRO);
        when(mockStAXTransformingBodyProcessor.process(EXPECTED_PARSED_LINK, mockBodyProcessingContext)).thenReturn(EXPECTED_TRANSFORMED_LINK);

        when(mockBodyProcessingContext.getAttributeForImage("height", uuid)).thenReturn(EXPECTED_IMAGE_HEIGHT);
        when(mockBodyProcessingContext.getAttributeForImage("width", uuid)).thenReturn(EXPECTED_IMAGE_WIDTH);
        when(mockBodyProcessingContext.getAttributeForImage("alt", uuid)).thenReturn(EXPECTED_IMAGE_ALT);
        when(mockBodyProcessingContext.getAttributeForImage("src", uuid)).thenReturn(EXPECTED_IMAGE_URL);
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
        assertTrue(promoBoxData.isOkToRender());
        assertEquals("Title was not as expected", EXPECTED_PARSED_TITLE, promoBoxData.getTitle());
        assertEquals("Headline was not as expected", EXPECTED_PARSED_HEADLINE, promoBoxData.getHeadline());
        assertEquals("Intro was not as expected", EXPECTED_PARSED_INTRO, promoBoxData.getIntro());
        assertEquals("Link was not as expected", EXPECTED_PARSED_LINK, promoBoxData.getLink());
        assertEquals("Image file ref was not as expected", EXPECTED_PARSED_IMAGE_FILE_REF, promoBoxData.getImageFileRef());

    }

    @Test
    public void testTransformFieldContentToStructuredFormat() {
        when(mockPromoBoxData.getTitle()).thenReturn(EXPECTED_PARSED_TITLE);
        when(mockPromoBoxData.getHeadline()).thenReturn(EXPECTED_PARSED_HEADLINE);
        when(mockPromoBoxData.getIntro()).thenReturn(EXPECTED_PARSED_INTRO);
        when(mockPromoBoxData.getLink()).thenReturn(EXPECTED_PARSED_LINK);
        when(mockPromoBoxData.getImageFileRef()).thenReturn(EXPECTED_PARSED_IMAGE_FILE_REF);

        promoBoxXMLParser.transformFieldContentToStructuredFormat(mockPromoBoxData, mockBodyProcessingContext);

        verify(mockStAXTransformingBodyProcessor).process(EXPECTED_PARSED_TITLE, mockBodyProcessingContext);
        verify(mockStAXTransformingBodyProcessor).process(EXPECTED_PARSED_HEADLINE, mockBodyProcessingContext);
        verify(mockStAXTransformingBodyProcessor).process(Mockito.eq(EXPECTED_PARSED_INTRO), Mockito.eq(mockBodyProcessingContext));
        verify(mockStAXTransformingBodyProcessor).process(Mockito.eq(EXPECTED_PARSED_LINK), Mockito.eq(mockBodyProcessingContext));

        verify(mockPromoBoxData).setImageType(EXPECTED_IMAGE_TYPE);
        verify(mockPromoBoxData).setImageHeight(EXPECTED_IMAGE_HEIGHT);
        verify(mockPromoBoxData).setImageWidth(EXPECTED_IMAGE_WIDTH);
        verify(mockPromoBoxData).setImageAlt(EXPECTED_IMAGE_ALT);
        verify(mockBodyProcessingContext, Mockito.never()).getAttributeForImage("type", uuid);
    }
}
