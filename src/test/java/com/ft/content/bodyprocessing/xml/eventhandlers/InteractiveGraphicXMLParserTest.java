package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.junit.Before;
import org.junit.Test;


public class InteractiveGraphicXMLParserTest extends BaseXMLParserTest {
    
    private static final String EXPECTED_SRC = "http://interactive.ftdata.co.uk/interactive-graphic-1234.js";
    private static final String EXPECTED_ID = "1234";
    private XMLEventReader xmlEventReader;
    private String validXml = "<plainHtml><div id=\"ig1234\"><script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"interactive-graphic\"></script></div></plainHtml>";
    private String xmlWithNoDivOrScriptTag = "<plainHtml> <h1>This is a heading</h1> <h2>This is a heading</h2> <h3>This is a heading</h3></plainHtml>";
    private String xmlWithIdNotPrefixedWithIG = "<plainHtml><div id=\"1234\"><script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"interactive-graphic\"></script></div></plainHtml>";
    private String xmlWithNoScriptDataAccessType = "<plainHtml><div id=\"ig1234\"><script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\"></script></div></plainHtml>";
    private String xmlWithScriptDataAccessTypeNotInteractiveGraphic = "<plainHtml><div id=\"ig1234\"><script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"SOME_OTHER_TYPE\"></script></div></plainHtml>";
    private String xmlWithAnInvalidSrc = "<plainHtml><div id=\"ig1234\"><script type=\"text/javascript\" src=\"not a valid url\" data-asset-type=\"interactive-graphic\"></script></div></plainHtml>";
    private String xmlWithElementsNotInOrderDivDivScript = "<plainHtml><div id=\"ig1234\"><div id=\"ig1234\"></div><script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"interactive-graphic\"></script></div></plainHtml>";
    private String xmlWithMissingDivId = "<plainHtml><div><script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"interactive-graphic\"></script></div></plainHtml>";
    private String xmlWithExtraElementsAfterDiv = "<plainHtml><div id=\"ig1234\"><td></td><script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"interactive-graphic\"></script></div></plainHtml>";
    private String xmlWithExtraElementsAfterPlainHtml = "<plainHtml><td></td><div id=\"ig1234\"><script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"interactive-graphic\"></script></div></plainHtml>";
    private String validXmlWithSpaces = "<plainHtml>      <div id=\"ig1234\">   <script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"interactive-graphic\">   </script> </div> </plainHtml>";
    private String validXmlWithCharactersInsideScript = "<plainHtml>      <div id=\"ig1234\">   <script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"interactive-graphic\"> some characters including /*comments*/  </script> </div> </plainHtml>";
    private String invalidXmlWithCharactersBetweenDivAndScript = "<plainHtml><div id=\"ig1234\">some unexpected characters<script type=\"text/javascript\" src=\"http://interactive.ftdata.co.uk/interactive-graphic-1234.js\" data-asset-type=\"interactive-graphic\"></script></div></plainHtml>";
    
    private BaseXMLParser<InteractiveGraphicData> interactiveGraphicXMLParser;
    
    @Before
    public void setUp() {
        interactiveGraphicXMLParser = new InteractiveGraphicXMLParser();
    }
    
    @Test
    public void testParseElementData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXml);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertTrue(interactiveGraphicData.isAllRequiredDataPresent());
        assertEquals("Id was not as expected",EXPECTED_ID, interactiveGraphicData.getId());
        assertEquals("Src was not as expected", EXPECTED_SRC, interactiveGraphicData.getSrc());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testParseElementDataWithSpaces() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXmlWithSpaces);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertTrue(interactiveGraphicData.isAllRequiredDataPresent());
        assertEquals("Id was not as expected",EXPECTED_ID, interactiveGraphicData.getId());
        assertEquals("Src was not as expected", EXPECTED_SRC, interactiveGraphicData.getSrc());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testParseElementDataNoScriptTag() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlWithNoDivOrScriptTag);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testParseElementDataNoScriptDataAccessType() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlWithNoScriptDataAccessType);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testParseElementDataNoIGPrefix() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlWithIdNotPrefixedWithIG);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testParseElementScriptDataAccessTypeNotInteractiveGraphic() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlWithScriptDataAccessTypeNotInteractiveGraphic);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testParseElementWithAnInvalidSrc() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlWithAnInvalidSrc);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testParseWithMissingDivId() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlWithMissingDivId);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testParseWithElementsNotInOrderDivDivScript() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlWithElementsNotInOrderDivDivScript);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testParseWithExtraElementsAfterPlainHtml() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlWithExtraElementsAfterPlainHtml);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testParseWithExtraElementsAfterDiv() throws XMLStreamException {
        xmlEventReader = createReaderForXml(xmlWithExtraElementsAfterDiv);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testParseWithCharactersBetweenDivAndScript() throws XMLStreamException {
        xmlEventReader = createReaderForXml(invalidXmlWithCharactersBetweenDivAndScript);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertFalse(interactiveGraphicData.isAllRequiredDataPresent());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }

    @Test
    public void testParseWithCharactersInsideScript() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXmlWithCharactersInsideScript);
        StartElement startElement = getStartElement(xmlEventReader);
        InteractiveGraphicData interactiveGraphicData = interactiveGraphicXMLParser
                .parseElementData(startElement, xmlEventReader);

        assertNotNull("interactiveGraphicData should not be null", interactiveGraphicData);
        assertTrue(interactiveGraphicData.isAllRequiredDataPresent());
        assertEquals("Id was not as expected", EXPECTED_ID, interactiveGraphicData.getId());
        assertEquals("Src was not as expected", EXPECTED_SRC, interactiveGraphicData.getSrc());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
}
