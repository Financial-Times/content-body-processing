package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;

public interface XmlParser<T> {
    
    T parseElementData(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException;
    void transformFieldContentToStructuredFormat(T dataBean, BodyProcessingContext bodyProcessingContext);
}
