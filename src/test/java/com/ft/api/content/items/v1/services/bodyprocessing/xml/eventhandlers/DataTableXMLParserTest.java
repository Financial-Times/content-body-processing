package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
public class DataTableXMLParserTest extends BaseXMLParserTest {

    @Mock private StAXTransformingBodyProcessor mockStAXTransformingBodyProcessor;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    
    private XMLEventReader xmlEventReader;
    
	private DataTableXMLParser dataTableXMLParser;

	private String validXml = "<table class=\"web-table\"><tr><td>some nested content</td></tr></table>";
	private String validXmlNoContents = "<table class=\"web-table\"></table>";
	
	@Before
    public void setUp() {
        dataTableXMLParser = new DataTableXMLParser(mockStAXTransformingBodyProcessor);
        when(mockStAXTransformingBodyProcessor.process(validXml, mockBodyProcessingContext)).thenReturn(validXml);
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithStAXTransformingBodyNull() {
        new DataTableXMLParser(null);
    }
    
    @Test
    public void testParseChildElementData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXml);
        StartElement startElement = getStartElement(xmlEventReader);
        DataTableData dataTableData = dataTableXMLParser.parseElementData(startElement, xmlEventReader);

        assertNotNull("DataTableData should not be null", dataTableData);
        assertTrue(dataTableData.isAllRequiredDataPresent());
        assertEquals("Body was not as expected", validXml, dataTableData.getBody().trim());
        assertFalse("xmlReader should have no more events", xmlEventReader.hasNext());
    }
    
    @Test
    public void testParseChildElementDataNoContents() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXmlNoContents);
        StartElement startElement = getStartElement(xmlEventReader);
        DataTableData dataTableData = dataTableXMLParser.parseElementData(startElement, xmlEventReader);

        assertNotNull("DataTableData should not be null", dataTableData);
        assertTrue(dataTableData.isAllRequiredDataPresent());
        assertEquals("Body was not as expected", validXmlNoContents, dataTableData.getBody().trim());
        assertFalse("xmlReader should have no more events", xmlEventReader.hasNext());
    }
    
    @Test
    public void testTransformFieldContentToStructuredFormat() throws XMLStreamException {
        DataTableData dataTableData = new DataTableData();
        dataTableData.setBody(validXml);
        dataTableXMLParser.transformFieldContentToStructuredFormat(dataTableData, mockBodyProcessingContext);

        assertNotNull("DataTableData should not be null", dataTableData);
        assertTrue(dataTableData.isAllRequiredDataPresent());
        assertEquals("Body was not as expected", validXml, dataTableData.getBody().trim());
    }
    
    @Test
    public void testTransformFieldContentToStructuredFormatNoContent() throws XMLStreamException {
        DataTableData dataTableData = new DataTableData();
        dataTableData.setBody(null);
        dataTableXMLParser.transformFieldContentToStructuredFormat(dataTableData, mockBodyProcessingContext);

        assertNotNull("DataTableData should not be null", dataTableData);
        assertFalse(dataTableData.isAllRequiredDataPresent());
        assertEquals("Body was not as expected", null, dataTableData.getBody());
    }

}
