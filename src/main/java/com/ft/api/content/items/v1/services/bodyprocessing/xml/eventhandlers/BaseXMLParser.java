package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public abstract class BaseXMLParser<T> {
    
    private String startElementName;

	protected BaseXMLParser(String startElementName) {
        notNull(startElementName, "The startElementName cannot be null!");
        this.startElementName = startElementName;
    }
    
    public T parseElementData(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException {
        T dataBean = createDataBeanInstance();
        
        try {
            // Use the start element (trigger element) to populate the bean as some types require parsing to start from the starting element
            populateBean(dataBean, startElement, xmlEventReader);
            
            while (xmlEventReader.hasNext()) {
                XMLEvent nextEvent = xmlEventReader.nextEvent();
                
                if(nextEvent.isStartElement()) {
                    StartElement nextStartElement = nextEvent.asStartElement();
                    populateBean(dataBean, nextStartElement, xmlEventReader);
                } else if(nextEvent.isEndElement()) {
                    // Check if it's the closing element of the start element, in which case exit as we should not continue to parse beyond this element.
                    if(isElementNamed(nextEvent.asEndElement().getName(), startElementName)) {
                        break;
                    }
                }
            }
        } catch (UnexpectedElementStructureException e) {
        	// Something went wrong - read til the end of this element to get rid of the whole thing
        	skipUntilMatchingEndTag(startElementName, xmlEventReader);
            // Ensure that the bean returned is not valid for processing.
            return createDataBeanInstance();
        }
        return dataBean;
    }

    abstract T createDataBeanInstance();

    protected boolean isElementNamed(QName elementName, String nameToMatch) {
        return elementName.getLocalPart().toLowerCase().equals(nameToMatch.toLowerCase());
    }

    abstract void populateBean(T dataBean, StartElement nextStartElement, XMLEventReader xmlEventReader) throws UnexpectedElementStructureException;
    
    private void skipUntilMatchingEndTag(String nameToMatch, XMLEventReader xmlEventReader) throws XMLStreamException {
		int count = 0;
		while (xmlEventReader.hasNext()) {
			XMLEvent event = xmlEventReader.nextEvent();
			if (event.isStartElement()) {
				StartElement newStartElement = event.asStartElement();
				if (nameToMatch
						.equals(newStartElement.getName().getLocalPart())) {
					count++;
				}
			}
			if (event.isEndElement()) {
				EndElement endElement = event.asEndElement();
				String localName = endElement.getName().getLocalPart();
				if (nameToMatch.equals(localName)) {
					if (count == 0) {
						return;
					}
					count--;
				}
			}
		}
	}
}
