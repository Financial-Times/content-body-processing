package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

public class StripElementAndContentsXMLEventHandler extends BaseXMLEventHandler {

	@Override
	public void handleStartElementEvent(StartElement startElement, XMLEventReader xmlEventReader, BodyWriter eventWriter,
			BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		
		String nameToMatch = startElement.getName().getLocalPart();
		skipUntilMatchingEndTag(nameToMatch, xmlEventReader);

	}

}
