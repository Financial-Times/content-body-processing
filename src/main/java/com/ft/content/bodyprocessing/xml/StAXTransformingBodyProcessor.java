package com.ft.content.bodyprocessing.xml;

import static org.springframework.util.Assert.notNull;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.content.bodyprocessing.BodyProcessor;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.ft.content.bodyprocessing.writer.BodyWriterFactory;
import com.ft.content.bodyprocessing.writer.HTML5VoidElementHandlingXMLBodyWriterFactory;
import com.ft.content.bodyprocessing.xml.eventhandlers.XMLEventHandler;
import com.ft.content.bodyprocessing.xml.eventhandlers.XMLEventHandlerRegistry;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.XMLEvent;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;


public class StAXTransformingBodyProcessor implements BodyProcessor {

    private BodyWriterFactory bodyWriterFactory;
    private XMLEventHandlerRegistry eventHandlerRegistry;
    private XMLEventReaderFactory xmlEventReaderFactory;

    public StAXTransformingBodyProcessor(XMLEventReaderFactory xmlEventReaderFactory,
            XMLEventHandlerRegistry eventHandlerRegistry, BodyWriterFactory bodyWriterFactory) {

        notNull(xmlEventReaderFactory, "xmlEventReaderFactory cannot be null");
        notNull(eventHandlerRegistry, "eventHandlerRegistry cannot be null");
        notNull(bodyWriterFactory, "bodyWriterFactory cannot be null");

        this.xmlEventReaderFactory = xmlEventReaderFactory;
        this.eventHandlerRegistry = eventHandlerRegistry;
        this.bodyWriterFactory = bodyWriterFactory;
    }
    
    public StAXTransformingBodyProcessor(XMLEventHandlerRegistry eventHandlerRegistry) {
        this(createXMLEventReaderFactory(), eventHandlerRegistry, createBodyWriterFactory());
    }

    @Override
    public String process(String body, BodyProcessingContext bodyProcessingContext) throws BodyProcessingException {

        XMLEventReader xmlEventReader = null;
        try {
            xmlEventReader = xmlEventReaderFactory.createXMLEventReader(body);
            
            return doProcess(xmlEventReader, bodyProcessingContext);

        } catch (XMLStreamException e) {
            throw new BodyProcessingException(e);
        } finally {
            if (xmlEventReader != null) {
                try {
                    xmlEventReader.close();
                } catch (XMLStreamException e) {
                    throw new BodyProcessingException(e);
                }
            }
        }
    }

    private String doProcess(XMLEventReader xmlEventReader, BodyProcessingContext bodyProcessingContext) 
            throws XMLStreamException {
        
        BodyWriter bodyWriter = null;
        try {
            bodyWriter = bodyWriterFactory.createBodyWriter();
            
            processEvents(xmlEventReader, bodyProcessingContext, bodyWriter);
            
            return bodyWriter.asString();
        }  finally {
            if (bodyWriter != null) {
                bodyWriter.close();
            }
        }
    }

    private void processEvents(XMLEventReader xmlEventReader, BodyProcessingContext bodyProcessingContext, BodyWriter bodyWriter) 
            throws XMLStreamException {
        while (xmlEventReader.hasNext()) {
            handleEvent(xmlEventReader.nextEvent(), xmlEventReader, bodyProcessingContext, bodyWriter);
        }
    }

    private void handleEvent(XMLEvent event, XMLEventReader xmlEventReader, BodyProcessingContext bodyProcessingContext, BodyWriter bodyWriter) throws XMLStreamException {
        XMLEventHandler eventHandler = null;
        if (event.isStartElement()) {
            eventHandler = eventHandlerRegistry.getEventHandler(event.asStartElement());
            eventHandler.handleStartElementEvent(event.asStartElement(), xmlEventReader, bodyWriter, bodyProcessingContext);
        } else if (event.isEndElement()) {
            eventHandler = eventHandlerRegistry.getEventHandler(event.asEndElement());
            eventHandler.handleEndElementEvent(event.asEndElement(), xmlEventReader, bodyWriter);
        } else if (event.isCharacters()) {
            eventHandler = eventHandlerRegistry.getEventHandler(event.asCharacters());
            eventHandler.handleCharactersEvent(event.asCharacters(), xmlEventReader, bodyWriter);
        } else if (event.getEventType() == XMLStreamConstants.COMMENT){
        	eventHandler = eventHandlerRegistry.getEventHandler((Comment)event);
        	eventHandler.handleComment((Comment)event, xmlEventReader, bodyWriter);
    	} else if (event.getEventType() == XMLStreamConstants.ENTITY_REFERENCE) {
    		eventHandler = eventHandlerRegistry.getEventHandler((EntityReference) event);
    		eventHandler.handleEntityReferenceEvent((EntityReference) event, xmlEventReader, bodyWriter);
    	} else {
            eventHandler = eventHandlerRegistry.getEventHandler(event);
            eventHandler.handleEvent(event, xmlEventReader, bodyWriter);
        }
    }

    private static BodyWriterFactory createBodyWriterFactory() {
        return new HTML5VoidElementHandlingXMLBodyWriterFactory((XMLOutputFactory2) XMLOutputFactory.newInstance());
    }

    private static XMLEventReaderFactory createXMLEventReaderFactory() {
        return new XMLEventReaderFactory((XMLInputFactory2) XMLInputFactory.newInstance());
    }
}
