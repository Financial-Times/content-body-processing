package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import java.io.StringWriter;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class ElementRawDataParser {

    /**
     * Extracts the contents of an element until its end element is reached. It assumes that the XMLElementReader is pointing to the start element.
     * @param endElementName
     * @param xmlEventReader
     * @return
     * @throws XMLStreamException
     */
    public String parse(String endElementName, XMLEventReader xmlEventReader) throws XMLStreamException {
        return this.parse(endElementName, xmlEventReader, null);
    }

    /**
     * Parses the contents starting at the next element in the XML Reader until the end tag (endElementName) if found.
     * Should the end element itself need to be included, the startElement should be provided which is used to wrap the contents and 
     * an end tag is written to ensure the content parsed is valid XML.
     * @param endElementName
     * @param xmlEventReader
     * @param startElement
     * @return
     * @throws XMLStreamException
     */
	public String parse(String endElementName, XMLEventReader xmlEventReader, StartElement startElement) throws XMLStreamException {
		StringWriter writer = new StringWriter();
		        boolean hasReachedEndElement = false;
		        ensureEndElementNameMatchesStartElement(endElementName, startElement);
		        
				// Wrap with start element based on the startElement
		        if(needsRootElementWrapping(startElement)) {
					startElement.writeAsEncodedUnicode(writer);
				}

		        while(xmlEventReader.hasNext() && !hasReachedEndElement) {
		            XMLEvent childEvent = xmlEventReader.nextEvent();

		            // Check if the closing element reached
		            if(childEvent.isEndElement()) {
		                if(isElementNamed(childEvent.asEndElement().getName(), endElementName)) {
		                    hasReachedEndElement = true;
							
		                    if(needsRootElementWrapping(startElement)) {
								// Wrap end element based on the endElement
		                        childEvent.writeAsEncodedUnicode(writer);
							}
		                    continue;
		                }
		            }
		            childEvent.writeAsEncodedUnicode(writer);
		        }
		        if(!hasReachedEndElement) {
		            throw new IllegalArgumentException(String.format("Element name mismatch, could not find the end element for %s", endElementName));
		        }
		        return writer.toString();
	}

    
    private void ensureEndElementNameMatchesStartElement(String endElementName, StartElement startElement) {
        String startElementName = parseStartElementName(startElement);
        if(startElementName != null && !startElementName.equals(endElementName.toLowerCase())) {
            throw new IllegalArgumentException("The endElementName and the StartElement must match!");
        }
    }

    private String parseStartElementName(StartElement startElement) {
        if(startElement !=null) {
            return  startElement.getName().getLocalPart().toLowerCase();
        }
        return null;
    }

    private boolean needsRootElementWrapping(StartElement startElement) {
        return startElement != null;
    }

    private boolean isElementNamed(QName elementName, String nameToMatch) {
        return elementName.getLocalPart().toLowerCase().equals(nameToMatch.toLowerCase());
    }

}
