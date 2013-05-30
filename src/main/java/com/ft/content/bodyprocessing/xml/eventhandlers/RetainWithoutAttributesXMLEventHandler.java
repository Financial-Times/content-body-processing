package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public class RetainWithoutAttributesXMLEventHandler extends BaseXMLEventHandler {
	
	@Override
	public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
		eventWriter.writeEndTag(event.getName().getLocalPart());
	}

	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter,
			BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		eventWriter.writeStartTag(event.getName().getLocalPart(), null);
	}
}
