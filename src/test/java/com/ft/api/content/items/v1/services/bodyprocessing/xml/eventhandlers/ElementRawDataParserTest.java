package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import junit.framework.Assert;
import org.junit.Test;

public class ElementRawDataParserTest extends BaseXMLParserTest {

    private XMLEventReader xmlEventReader;
    
    private String element = "some-element";
    private String rawElementContent = "<p></p>some more stuff<table><tr><td>test</td></tr>more text</table>testing 12345";
    private String validXml = "<".concat(element).concat(">").concat(rawElementContent).concat("</").concat(element).concat(">");
    
    @Test
    public void shouldParseElementContents() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXml);
        moveReaderToElement(xmlEventReader, element);

        ElementRawDataParser elementRawDataParser = new ElementRawDataParser();
        String actualRawContent = elementRawDataParser.parse("some-element", xmlEventReader);
        Assert.assertEquals(rawElementContent, actualRawContent);
    }

	@Test
	    public void shouldParseElementContentsIncludingRootElement() throws XMLStreamException {
	        xmlEventReader = createReaderForXml(validXml);
	        //moveReaderToElement(xmlEventReader, "html");

	        ElementRawDataParser elementRawDataParser = new ElementRawDataParser();

			StartElement startElement = getStartElement(xmlEventReader);
	        String actualRawContent = elementRawDataParser.parse("some-element", xmlEventReader, startElement);
	        Assert.assertEquals(validXml, actualRawContent);
	    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWithMismatchElementName() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXml);
        moveReaderToElement(xmlEventReader, element);
        
        ElementRawDataParser elementRawDataParser = new ElementRawDataParser();
        String rawElementContent = elementRawDataParser.parse("mismatch-element", xmlEventReader);
        Assert.assertEquals(rawElementContent, rawElementContent);
    }
    
    private void moveReaderToElement(XMLEventReader xmlEventReader, String element) throws XMLStreamException {
        while(xmlEventReader.hasNext()) {
            XMLEvent nextEvent = xmlEventReader.nextEvent();
            if(nextEvent.isStartElement()) {
                if(nextEvent.asStartElement().getName().getLocalPart().equals(element)) {
                    break;
                }
            }
        }
    }
}
