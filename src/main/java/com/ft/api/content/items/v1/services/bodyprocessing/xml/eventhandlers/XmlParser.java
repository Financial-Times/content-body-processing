package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;

public interface XmlParser<T> {
    
    T parseElementData(XMLEventReader xmlEventReader) throws XMLStreamException;
    void transformFieldContentToStructuredFormat(T dataBean, BodyProcessingContext bodyProcessingContext);
}
