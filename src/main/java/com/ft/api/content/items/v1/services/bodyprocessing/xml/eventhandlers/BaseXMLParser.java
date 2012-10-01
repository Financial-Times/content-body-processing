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
        
        while (xmlEventReader.hasNext()) {
            XMLEvent nextEvent = xmlEventReader.nextEvent();
            
            if(nextEvent.isStartElement()) {
                StartElement nextStartElement = nextEvent.asStartElement();
                populateBeanUsingStartElement(dataBean, nextStartElement, xmlEventReader);
                
            } else if(nextEvent.isEndElement()) {
                // Check if it's the closing element of the start element, in which case exit as we should not continue to parse beyond this element.
                if(isElementNamed(nextEvent.asEndElement().getName(), startElementName)) {
                    break;
                }
            }
        }
        return dataBean;
    }

    abstract T createDataBeanInstance();

    protected boolean isElementNamed(QName elementName, String nameToMatch) {
        return elementName.getLocalPart().toLowerCase().equals(nameToMatch);
    }
    //TODO: add Throws checked exception for flow/element order issues
    abstract void populateBeanUsingStartElement(T dataBean, StartElement nextStartElement, XMLEventReader xmlEventReader);
}
