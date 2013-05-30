package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SlideshowXMLParserTest extends BaseXMLParserTest {

    private static final Object EXPECTED_UUID = "fbb7e914-9404-11e1-8c6f-00144feab49a";
    private XMLEventReader xmlEventReader;
    private SlideshowXMLParser slideshowXMLParser;
    private String validXml = "<a type=\"slideshow\" href=\"/FT/Content/Weekend/Stories/Live/05Magweb_Slideshow_Rowley.gallery.xml?uuid=fbb7e914-9404-11e1-8c6f-00144feab49a\"><DIHeadlineCopy>Slideshow: Crème Dubarry</DIHeadlineCopy>\n</a>";
    private String validXmlNoType = "<a href=\"/FT/Content/Weekend/Stories/Live/05Magweb_Slideshow_Rowley.gallery.xml?uuid=fbb7e914-9404-11e1-8c6f-00144feab49a\"><DIHeadlineCopy>Slideshow: Crème Dubarry</DIHeadlineCopy>\n</a>";
    private String validXmlExtraParameter = "<a type=\"slideshow\" href=\"/FT/Content/Weekend/Stories/Live/05Magweb_Slideshow_Rowley.gallery.xml?t=1&amp;uuid=fbb7e914-9404-11e1-8c6f-00144feab49a\"><DIHeadlineCopy>Slideshow: Crème Dubarry</DIHeadlineCopy>\n</a>";
    private String invalidXmlMissingUUIDValue = "<a type=\"slideshow\" href=\"/FT/Content/Weekend/Stories/Live/05Magweb_Slideshow_Rowley.gallery.xml?uuid=\"><DIHeadlineCopy>Slideshow: Crème Dubarry</DIHeadlineCopy>\n</a>";
    private String invalidXmlMissingUUIDKey = "<a type=\"slideshow\" href=\"/FT/Content/Weekend/Stories/Live/05Magweb_Slideshow_Rowley.gallery.xml?fbb7e914-9404-11e1-8c6f-00144feab49a\"><DIHeadlineCopy>Slideshow: Crème Dubarry</DIHeadlineCopy>\n</a>";
    private String invalidXmlNoParams = "<a type=\"slideshow\" href=\"/FT/Content/Weekend/Stories/Live/05Magweb_Slideshow_Rowley.gallery.xml\"><DIHeadlineCopy>Slideshow: Crème Dubarry</DIHeadlineCopy>\n</a>";
    private String invalidXmlNoHref = "<a type=\"slideshow\" hrefX=\"/FT/Content/Weekend/Stories/Live/05Magweb_Slideshow_Rowley.gallery.xml?uuid=fbb7e914-9404-11e1-8c6f-00144feab49a\"><DIHeadlineCopy>Slideshow: Crème Dubarry</DIHeadlineCopy>\n</a>";
    
    @Before
    public void setUp() {
        slideshowXMLParser = new SlideshowXMLParser();
    }
    
    @Test
    public void testDoesTriggerElementContainAllDataNeeded() {
        assertFalse(slideshowXMLParser.doesTriggerElementContainAllDataNeeded());
    }
    
    @Test
    public void testCreateDataBean() {
        SlideshowData slideshowData = slideshowXMLParser.createDataBeanInstance();
        assertNotNull(slideshowData);
    }
    
    @Test
    public void testPopulateBeanWithValidXml() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXml);
        StartElement startElement = getStartElement(xmlEventReader);
        SlideshowData slideshowData = slideshowXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("SlideshowData should not be null", slideshowData);
        assertTrue(slideshowData.isAllRequiredDataPresent());
        assertEquals("Uuid was not as expected", EXPECTED_UUID, slideshowData.getUuid());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testPopulateBeanWithValidXmlNoType() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXmlNoType);
        StartElement startElement = getStartElement(xmlEventReader);
        SlideshowData slideshowData = slideshowXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("SlideshowData should not be null", slideshowData);
        assertTrue(slideshowData.isAllRequiredDataPresent());
        assertEquals("Uuid was not as expected", EXPECTED_UUID, slideshowData.getUuid());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test 
    public void testPopulateBeanWithValidXmlExtraParameter() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXmlExtraParameter);
        StartElement startElement = getStartElement(xmlEventReader);
        SlideshowData slideshowData = slideshowXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("SlideshowData should not be null", slideshowData);
        assertTrue(slideshowData.isAllRequiredDataPresent());
        assertEquals("Uuid was not as expected", EXPECTED_UUID, slideshowData.getUuid());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test 
    public void testPopulateBeanWithInvalidXmlExtraParameter() throws XMLStreamException {
        xmlEventReader = createReaderForXml(invalidXmlMissingUUIDValue);
        StartElement startElement = getStartElement(xmlEventReader);
        SlideshowData slideshowData = slideshowXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("SlideshowData should not be null", slideshowData);
        assertFalse(slideshowData.isAllRequiredDataPresent());
        assertTrue("Uuid was not as expected", StringUtils.isBlank(slideshowData.getUuid()));
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test 
    public void testPopulateBeanWithInvalidXmlMissingUUIDKey() throws XMLStreamException {
        xmlEventReader = createReaderForXml(invalidXmlMissingUUIDKey);
        StartElement startElement = getStartElement(xmlEventReader);
        SlideshowData slideshowData = slideshowXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("SlideshowData should not be null", slideshowData);
        assertFalse(slideshowData.isAllRequiredDataPresent());
        assertTrue("Uuid was not as expected", StringUtils.isBlank(slideshowData.getUuid()));
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test 
    public void testPopulateBeanWithInvalidXmlNoParams() throws XMLStreamException {
        xmlEventReader = createReaderForXml(invalidXmlNoParams);
        StartElement startElement = getStartElement(xmlEventReader);
        SlideshowData slideshowData = slideshowXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("SlideshowData should not be null", slideshowData);
        assertFalse(slideshowData.isAllRequiredDataPresent());
        assertTrue("Uuid was not as expected", StringUtils.isBlank(slideshowData.getUuid()));
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test 
    public void testPopulateBeanWithInvalidXmlNoHref() throws XMLStreamException {
        xmlEventReader = createReaderForXml(invalidXmlNoHref);
        StartElement startElement = getStartElement(xmlEventReader);
        SlideshowData slideshowData = slideshowXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("SlideshowData should not be null", slideshowData);
        assertFalse(slideshowData.isAllRequiredDataPresent());
        assertTrue("Uuid was not as expected", StringUtils.isBlank(slideshowData.getUuid()));
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullValue() {
        new SlideshowXMLParser(null);
    }
}
