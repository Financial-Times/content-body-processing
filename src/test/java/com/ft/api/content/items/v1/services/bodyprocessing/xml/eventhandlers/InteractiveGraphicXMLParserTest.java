package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Before;
import org.junit.Test;


public class InteractiveGraphicXMLParserTest {
    
    private static final String EXPECTED_SRC = "http://interactive.ftdata.co.uk/interactive-graphic-1234.js";
    private static final String EXPECTED_ID = "1234";
    private XMLEventReader xmlEventReader;
    private String validXml = "<plainHtml><div id=\"ig1234\"><script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"interactive-graphic\"></script></div></plainHtml>";
    private String xmlWithIdNotPrefixedWithIG = "<plainHtml><div id=\"1234\"><script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"interactive-graphic\"></script></div></plainHtml>";
    private String xmlWithScriptDataAccessTypeNotInteractiveGraphic = "<plainHtml><div id=\"ig1234\"><script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"SOME_OTHER_TYPE\"></script></div></plainHtml>";
    private String xmlWithAnInvalidSrc = "<plainHtml><div id=\"ig1234\"><script type=\"text/javascript\" src=\"not a valid url\" data-asset-type=\"interactive-graphic\"></script></div></plainHtml>";
    
    private InteractiveGraphicXMLParser interactiveGraphicXMLParser;
    
    @Before
    public void setUp() {
        interactiveGraphicXMLParser = new InteractiveGraphicXMLParser();
    }
    
    @Test
    public void testParseElementData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXml);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertTrue(interactiveGraphicData.isOkToRender());
        assertEquals("Id was not as expected",EXPECTED_ID, interactiveGraphicData.getId());
        assertEquals("Src was not as expected", EXPECTED_SRC, interactiveGraphicData.getSrc());
    }
    
    @Test
    public void testParseElementDataNoIGPrefix() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlWithIdNotPrefixedWithIG);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isOkToRender());
    }
    
    @Test
    public void testParseElementScriptDataAccessTypeNotInteractiveGraphic() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlWithScriptDataAccessTypeNotInteractiveGraphic);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isOkToRender());
    }
    
    @Test
    public void testParseElementWithAnInvalidSrc() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlWithAnInvalidSrc);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isOkToRender());
    }
    
    private XMLEventReader createReaderForXml(String xml) throws XMLStreamException {
        XMLInputFactory newInstance = XMLInputFactory2.newInstance();
        StringReader reader = new StringReader(xml);
        return newInstance.createXMLEventReader(reader);
    }
}
