package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public abstract class BaseXMLParser<T> {
    
    private String startElementName;

    protected BaseXMLParser(String startElementName) {
        notNull(startElementName, "The startElementName cannot be null!");
        this.startElementName = startElementName;
    }
    
    public T parseElementData(XMLEventReader xmlEventReader) throws XMLStreamException {
        T dataBean = createDataBeanInstance();
        
        try {
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
}
