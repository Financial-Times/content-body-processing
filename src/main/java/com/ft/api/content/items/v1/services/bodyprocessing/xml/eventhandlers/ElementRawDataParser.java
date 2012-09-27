package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class ElementRawDataParser {

    /**
     * Extracts the contents of an element until its end element is reached. It assumes that the XMLElementReader is pointing to the start element.
     * @param elementName
     * @param xmlEventReader
     * @return
     * @throws XMLStreamException
     */
    public String parse(String elementName, XMLEventReader xmlEventReader) throws XMLStreamException {
        StringWriter writer = new StringWriter();
        boolean hasReachedEndElement = false;
        
        while(xmlEventReader.hasNext() && !hasReachedEndElement) {
            XMLEvent childEvent = xmlEventReader.nextEvent();
            
            // Check if the closing element reached
            if(childEvent.isEndElement()) {
                if(isElementNamed(childEvent.asEndElement().getName(), elementName)) {
                    hasReachedEndElement = true;
                    continue;
                }
            }
            
            childEvent.writeAsEncodedUnicode(writer);
        }
        if(!hasReachedEndElement) {
            throw new IllegalArgumentException(String.format("Element name mismatch, could not find the end element for %s", elementName));
        }
        return writer.toString();
    }
    
    private boolean isElementNamed(QName elementName, String nameToMatch) {
        return elementName.getLocalPart().toLowerCase().equals(nameToMatch.toLowerCase());
    }

}
