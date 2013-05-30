package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public interface XMLEventHandler {

	public void handleEvent(XMLEvent event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException;
	public void handleCharactersEvent(Characters event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException;	
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext) throws XMLStreamException;
	public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException;
	public void handleComment(Comment event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException;
	public void handleEntityReferenceEvent(EntityReference event, XMLEventReader xmlEventReader, BodyWriter bodyWriter) throws XMLStreamException;

}
