package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;

public class InteractiveGraphicXMLParser extends BaseXMLParser<InteractiveGraphicData> implements
        XmlParser<InteractiveGraphicData> {

    private static final String VALID_ID_PREFIX = "ig";
    private static final String SCRIPT_ATTRIBUTE_SRC = "src";
    private static final String SCRIPT = "script";
    private static final String DIV_ATTRIBUTE_ID = "id";
    private static final String DIV = "div";
    private static final String PLAIN_HTML = "plainHtml";
    private static final String VALID_DATA_ACCESS_TYPE = "interactive-graphic";
    private final QName scriptDataAccessTypeAttribute = new QName("data-asset-type");

    protected InteractiveGraphicXMLParser() {
        super(PLAIN_HTML);
    }
    
    @Override
    public void transformFieldContentToStructuredFormat(InteractiveGraphicData dataBean, BodyProcessingContext bodyProcessingContext) {
        //Do nothing as this is not supported/needed for Interactive Graphic elements
    }

    @Override
    protected InteractiveGraphicData createDataBeanInstance() {
        return new InteractiveGraphicData();
    }

    @Override
    void populateBean(InteractiveGraphicData interactiveGraphicData, StartElement startElement,
            XMLEventReader xmlEventReader) {
        
        QName elementName = startElement.getName();
        if(isElementNamed(elementName, PLAIN_HTML)) {
            enforceStrictElementStructure(true, DIV, xmlEventReader);
        } 
        else if (isElementNamed(elementName, DIV)) {
            String id = parseAttribute(DIV_ATTRIBUTE_ID, startElement);
            interactiveGraphicData.setId(extractId(id));
            enforceStrictElementStructure(true, SCRIPT, xmlEventReader);
        } 
        else if (isElementNamed(elementName, SCRIPT)) {
            if (isScriptCorrectType(startElement)) {
                String src = parseAttribute(SCRIPT_ATTRIBUTE_SRC, startElement);
                interactiveGraphicData.setSrc(extractSrc(src));
                enforceStrictElementStructure(false, SCRIPT, xmlEventReader);
            }
        } // check if the end element has been reached
        else if(!isElementNamed(elementName, PLAIN_HTML)) {
            // otherwise throw an exception since it's not an element that's expected for Interactive Graphics
            throw new UnexpectedElementStructureException(String.format("Found unsupported element [%s] while parsing the Interactive Graphic element", elementName));
        }
    }

    private void enforceStrictElementStructure(boolean isStartElementExpected, String expectedElementName, XMLEventReader xmlEventReader) {
       XMLEvent xmlEvent = peekNextElement(xmlEventReader);
       
       // Check for spaces, such events should be ignored and not fail the processing of the element
       if(xmlEvent.isCharacters() && isWhiteSpace(xmlEvent.asCharacters())) {
           return;
       }
       
       if(isStartElementExpected && xmlEvent.isStartElement()) {
           StartElement startElement = xmlEvent.asStartElement();
           validateElementName(expectedElementName, startElement.getName());
       } else if(!isStartElementExpected && xmlEvent.isEndElement()) {
           EndElement endtElement = xmlEvent.asEndElement();
           validateElementName(expectedElementName, endtElement.getName());
       } else {
           throw new UnexpectedElementStructureException(String.format("Found unsupported element while parsing the Interactive Graphic element"));
       }
        
    }

    private boolean isWhiteSpace(Characters characters) {
        String parsedText = characters.asCharacters().getData();
        return parsedText.trim().equals(StringUtils.EMPTY);
    }

    
    private void validateElementName(String expectedElementName, QName actualElementName) {
       if(!isElementNamed(actualElementName, expectedElementName)) {
           throw new UnexpectedElementStructureException(String.format("Found unsupported element while parsing the Interactive Graphic element Expected[%s] Actual[%s]", expectedElementName, actualElementName.getLocalPart().toString()));
       }
    }

    private XMLEvent peekNextElement(XMLEventReader xmlEventReader) {
        try {
            return xmlEventReader.peek();
        } catch (XMLStreamException e) {
           throw new UnexpectedElementStructureException(e.getMessage());
        }
    }

    private String parseAttribute(String attributeName, StartElement startElement) {
      Attribute attributeValue = startElement.getAttributeByName(new QName(attributeName));
      if(attributeValue == null || StringUtils.isBlank(attributeValue.getValue())) {
          throw new UnexpectedElementStructureException(String.format("Failed to parse attribute [%s]", attributeName));
      }
      return attributeValue.getValue();
    }

    /**
     * Check whether the src contains a valid URL structure and return the input
     * value otherwise return null;
     * 
     * @param src
     * @return
     */
    private String extractSrc(String src) {
        try {
            new URL(src);
            // No exception thrown means that the URL is valid (according to java.net.URL)
            return src;
        } catch (MalformedURLException e) {
            // Not a valid URL. Don't parse the URL.
            return null;
        }
    }

    private boolean isScriptCorrectType(StartElement nextStartElement) {
        Attribute dataAccessType = nextStartElement.getAttributeByName(scriptDataAccessTypeAttribute);
        return dataAccessType != null && dataAccessType.getValue().equals(VALID_DATA_ACCESS_TYPE);
    }

    private String extractId(String id) {
        if (id.startsWith(VALID_ID_PREFIX)) {
            return id.substring(2);
        }
        return null;
    }
}
