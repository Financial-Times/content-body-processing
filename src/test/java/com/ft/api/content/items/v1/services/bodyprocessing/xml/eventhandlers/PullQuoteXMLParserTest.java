package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers.PullQuoteData;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers.PullQuoteXMLParser;
import java.io.StringReader;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

public class PullQuoteXMLParserTest {

    private XMLEventReader xmlEventReader;
    private String validXml = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><web-pull-quote-text><p>92 mm</p></web-pull-quote-text></td></tr><tr><td><web-pull-quote-source>highest rainfall recorded in one hour, Maidenhead, July 12 1901</web-pull-quote-source></td></tr></table></web-pull-quote></p>";
    private String xmlMissingQuoteTextElement = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td></td></tr><tr><td><web-pull-quote-source>highest rainfall recorded in one hour, Maidenhead, July 12 1901</web-pull-quote-source></td></tr></table></web-pull-quote></p>";
    private String xmlMissingQuoteSourceElement = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><web-pull-quote-text><p>92 mm</p></web-pull-quote-text></td></tr><tr><td></td></tr></table></web-pull-quote></p>";
    private String xmlMissingQuoteTextAndSourceElements = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td></td></tr><tr><td></td></tr></table></web-pull-quote></p><web-pull-quote-text><p>92 mm</p></web-pull-quote-text>";
    private String xmlEmptyQuoteText = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><web-pull-quote-text></web-pull-quote-text></td></tr><tr><td><web-pull-quote-source>highest rainfall recorded in one hour, Maidenhead, July 12 1901</web-pull-quote-source></td></tr></table></web-pull-quote></p>";
    private String xmlEmptySourceText = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><web-pull-quote-text><p>92 mm</p></web-pull-quote-text></td></tr><tr><td><web-pull-quote-source></web-pull-quote-source></td></tr></table></web-pull-quote></p>";
    private String xmlEmptyQuoteAndSourceText = "<p><web-pull-quote align=\"left\"><table align=\"left\" cellpadding=\"6px\" width=\"170px\"><tr><td><web-pull-quote-text></web-pull-quote-text></td></tr><tr><td><web-pull-quote-source></web-pull-quote-source></td></tr></table></web-pull-quote></p>";
     
    @Test
    public void testParseChildElementData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXml);
        PullQuoteXMLParser pullQuoteXMLParser = new PullQuoteXMLParser();
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(xmlEventReader);
        
        assertNotNull("PullQuoteData should not be null", pullQuoteData);
        assertTrue(pullQuoteData.isOkToRender());
        assertEquals("Text was not as expected","<p>92 mm</p>", pullQuoteData.getQuoteText());
        assertEquals("Source was not as expected", "highest rainfall recorded in one hour, Maidenhead, July 12 1901", pullQuoteData.getQuoteSource());
    }
    
    @Test
    public void testParseChildElementDataWithMissingQuoteTextElement() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlMissingQuoteTextElement);
        PullQuoteXMLParser pullQuoteXMLParser = new PullQuoteXMLParser();
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(xmlEventReader);
        assertNotNull("PullQuoteData should not be null", pullQuoteData);
        assertTrue(pullQuoteData.isOkToRender());
        assertNull("Text was not as expected", pullQuoteData.getQuoteText());
        assertEquals("Source was not as expected", "highest rainfall recorded in one hour, Maidenhead, July 12 1901", pullQuoteData.getQuoteSource());

    }
    
    @Test
    public void testParseChildElementDataWithMissingQuoteSourceElement() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlMissingQuoteSourceElement);
        PullQuoteXMLParser pullQuoteXMLParser = new PullQuoteXMLParser();
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(xmlEventReader);
        assertNotNull("PullQuoteData should not be null", pullQuoteData);
        assertTrue(pullQuoteData.isOkToRender());
        assertEquals("Text was not as expected","<p>92 mm</p>", pullQuoteData.getQuoteText());
        assertNull("Source was not as expected", pullQuoteData.getQuoteSource());
    }
    
    @Test
    public void testParseChildElementDataWithMissingQuoteTextAndSourceElements() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlMissingQuoteTextAndSourceElements);
        PullQuoteXMLParser pullQuoteXMLParser = new PullQuoteXMLParser();
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(xmlEventReader);
        assertNotNull(pullQuoteData);
        assertFalse(pullQuoteData.isOkToRender());
    }
    
    @Test
    public void testParseChildElementDataWithMissingQuoteTextAndSourceData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlEmptyQuoteAndSourceText);
        PullQuoteXMLParser pullQuoteXMLParser = new PullQuoteXMLParser();
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(xmlEventReader);
        assertNotNull(pullQuoteData);
        assertFalse(pullQuoteData.isOkToRender());
    }
    
    @Test
    public void testParseChildElementDataWithEmptyTextData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlEmptyQuoteText);
        PullQuoteXMLParser pullQuoteXMLParser = new PullQuoteXMLParser();
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(xmlEventReader);
        assertNotNull("PullQuoteData should not be null", pullQuoteData);
        assertTrue(pullQuoteData.isOkToRender());
        assertEquals("Text was not as expected", "", pullQuoteData.getQuoteText());
        assertEquals("Source was not as expected", "highest rainfall recorded in one hour, Maidenhead, July 12 1901", pullQuoteData.getQuoteSource());
    }
    
    @Test
    public void testParseChildElementDataWithEmptySourceData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlEmptySourceText);
        PullQuoteXMLParser pullQuoteXMLParser = new PullQuoteXMLParser();
        PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(xmlEventReader);
        assertNotNull("PullQuoteData should not be null", pullQuoteData);
        assertTrue(pullQuoteData.isOkToRender());
        assertEquals("Text was not as expected","<p>92 mm</p>", pullQuoteData.getQuoteText());
        assertEquals("Source was not as expected", "", pullQuoteData.getQuoteSource());

    }
    
    private XMLEventReader createReaderForXml(String xml) throws XMLStreamException {
        XMLInputFactory newInstance = XMLInputFactory2.newInstance();
        StringReader reader = new StringReader(xml);
        return newInstance.createXMLEventReader(reader);
    }
}
