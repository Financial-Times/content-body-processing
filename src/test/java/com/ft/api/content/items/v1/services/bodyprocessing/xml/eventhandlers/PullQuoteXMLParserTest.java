package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.StAXTransformingBodyProcessor;

@RunWith(MockitoJUnitRunner.class)
public class PullQuoteXMLParserTest extends BaseXMLParserTest {

    private static final String EXPECTED_SOURCE = "highest rainfall recorded in one hour, Maidenhead, July 12 1901";
    private static final String EXPECTED_TEXT = "<p>92 mm</p>";
    private XMLEventReader xmlEventReader;
    private String validXml = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><web-pull-quote-text><p>92 mm</p></web-pull-quote-text></td></tr><tr><td><web-pull-quote-source>highest rainfall recorded in one hour, Maidenhead, July 12 1901</web-pull-quote-source></td></tr></table></web-pull-quote></p>";
    private String xmlMissingQuoteTextElement = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td></td></tr><tr><td><web-pull-quote-source>highest rainfall recorded in one hour, Maidenhead, July 12 1901</web-pull-quote-source></td></tr></table></web-pull-quote></p>";
    private String xmlMissingQuoteSourceElement = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><web-pull-quote-text><p>92 mm</p></web-pull-quote-text></td></tr><tr><td></td></tr></table></web-pull-quote></p>";
    private String xmlMissingQuoteTextAndSourceElements = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td></td></tr><tr><td></td></tr></table></web-pull-quote></p><web-pull-quote-text><p>92 mm</p></web-pull-quote-text>";
    private String xmlEmptyQuoteText = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><web-pull-quote-text></web-pull-quote-text></td></tr><tr><td><web-pull-quote-source>highest rainfall recorded in one hour, Maidenhead, July 12 1901</web-pull-quote-source></td></tr></table></web-pull-quote></p>";
    private String xmlEmptySourceText = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><web-pull-quote-text><p>92 mm</p></web-pull-quote-text></td></tr><tr><td><web-pull-quote-source></web-pull-quote-source></td></tr></table></web-pull-quote></p>";
    private String xmlEmptyQuoteAndSourceText = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><web-pull-quote-text></web-pull-quote-text></td></tr><tr><td><web-pull-quote-source></web-pull-quote-source></td></tr></table></web-pull-quote></p>";
    
    private PullQuoteXMLParser pullQuoteXMLParser;
    @Mock private StAXTransformingBodyProcessor mockStAXTransformingBodyProcessor;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    
    @Before
    public void setUp() {
        pullQuoteXMLParser = new PullQuoteXMLParser(mockStAXTransformingBodyProcessor);
        when(mockStAXTransformingBodyProcessor.process(EXPECTED_SOURCE, mockBodyProcessingContext)).thenReturn(EXPECTED_SOURCE);
        when(mockStAXTransformingBodyProcessor.process(EXPECTED_TEXT, mockBodyProcessingContext)).thenReturn(EXPECTED_TEXT);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithStAXTransformingBodyNull() {
        new PullQuoteXMLParser(null);
    }
    
    @Test
    public void testParseChildElementData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXml);
        StartElement startElement = getStartElement(xmlEventReader);
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("PullQuoteData should not be null", pullQuoteData);
        assertTrue(pullQuoteData.isOkToRender());
        assertEquals("Text was not as expected",EXPECTED_TEXT, pullQuoteData.getQuoteText());
        assertEquals("Source was not as expected", EXPECTED_SOURCE, pullQuoteData.getQuoteSource());
    }

    @Test
    public void testParseChildElementDataWithMissingQuoteTextElement() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlMissingQuoteTextElement);
        StartElement startElement = getStartElement(xmlEventReader);
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(startElement, xmlEventReader);
        assertNotNull("PullQuoteData should not be null", pullQuoteData);
        assertTrue(pullQuoteData.isOkToRender());
        assertNull("Text was not as expected", pullQuoteData.getQuoteText());
        assertEquals("Source was not as expected", EXPECTED_SOURCE, pullQuoteData.getQuoteSource());
    }
    
    @Test
    public void testParseChildElementDataWithMissingQuoteSourceElement() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlMissingQuoteSourceElement);
        StartElement startElement = getStartElement(xmlEventReader);
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(startElement, xmlEventReader);
        assertNotNull("PullQuoteData should not be null", pullQuoteData);
        assertTrue(pullQuoteData.isOkToRender());
        assertEquals("Text was not as expected",EXPECTED_TEXT, pullQuoteData.getQuoteText());
        assertNull("Source was not as expected", pullQuoteData.getQuoteSource());
    }
    
    @Test
    public void testParseChildElementDataWithMissingQuoteTextAndSourceElements() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlMissingQuoteTextAndSourceElements);
        StartElement startElement = getStartElement(xmlEventReader);
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(startElement, xmlEventReader);
        assertNotNull(pullQuoteData);
        assertFalse(pullQuoteData.isOkToRender());
    }
    
    @Test
    public void testParseChildElementDataWithMissingQuoteTextAndSourceData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlEmptyQuoteAndSourceText);
        StartElement startElement = getStartElement(xmlEventReader);
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(startElement, xmlEventReader);
        assertNotNull(pullQuoteData);
        assertFalse(pullQuoteData.isOkToRender());
    }
    
    @Test
    public void testParseChildElementDataWithEmptyTextData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlEmptyQuoteText);
        StartElement startElement = getStartElement(xmlEventReader);
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(startElement, xmlEventReader);
        assertNotNull("PullQuoteData should not be null", pullQuoteData);
        assertTrue(pullQuoteData.isOkToRender());
        assertEquals("Text was not as expected", "", pullQuoteData.getQuoteText());
        assertEquals("Source was not as expected", EXPECTED_SOURCE, pullQuoteData.getQuoteSource());
    }
    
    @Test
    public void testParseChildElementDataWithEmptySourceData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlEmptySourceText);
        StartElement startElement = getStartElement(xmlEventReader);
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(startElement, xmlEventReader);
        assertNotNull("PullQuoteData should not be null", pullQuoteData);
        assertTrue(pullQuoteData.isOkToRender());
        assertEquals("Text was not as expected",EXPECTED_TEXT, pullQuoteData.getQuoteText());
        assertEquals("Source was not as expected", "", pullQuoteData.getQuoteSource());
    }
}
