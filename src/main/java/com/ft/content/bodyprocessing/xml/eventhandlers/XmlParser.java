package com.ft.content.bodyprocessing.xml.eventhandlers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import com.ft.content.bodyprocessing.BodyProcessingContext;

public interface XmlParser<T> {
    
    T parseElementData(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException;
    void transformFieldContentToStructuredFormat(T dataBean, BodyProcessingContext bodyProcessingContext);
}
