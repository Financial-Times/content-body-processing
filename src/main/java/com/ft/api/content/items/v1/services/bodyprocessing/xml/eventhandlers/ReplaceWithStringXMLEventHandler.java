package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;


public class ReplaceWithStringXMLEventHandler extends BaseXMLEventHandler {
	
	private String replacementString = null;

	public ReplaceWithStringXMLEventHandler(String replacementString) {
		this.replacementString = replacementString;
	}

	@Override
	public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader,
			BodyWriter eventWriter) throws XMLStreamException {
		eventWriter.write(replacementString);
	}

	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader,
			BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		eventWriter.write(replacementString);
	}
}
