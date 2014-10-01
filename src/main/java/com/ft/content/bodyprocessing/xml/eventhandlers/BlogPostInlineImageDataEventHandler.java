package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.BodyProcessor;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public abstract class BlogPostInlineImageDataEventHandler extends BaseXMLEventHandler {
    protected XMLEventHandler fallbackHandler;
    protected BodyProcessor transformingBodyProcessor;

    public BlogPostInlineImageDataEventHandler(XMLEventHandler fallbackHandler, BodyProcessor transformingBodyProcessor) {
        this.fallbackHandler = fallbackHandler;
        this.transformingBodyProcessor = transformingBodyProcessor;
    }

    @Override
    public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext)
            throws XMLStreamException {
        String imageId = retrieveImageId(event);
        if (imageId != null) {
            String imageData = transformRawContentToStructuredFormat(retrieveImageData(event, xmlEventReader), bodyProcessingContext);
            if (StringUtils.isNotEmpty(imageData)) {
                bodyProcessingContext.addAttributesToExistingImageWithId(imageId, ImmutableMap.of(getImageDataAttributeName(), imageData));
            }
        } else {
            fallbackHandler.handleStartElementEvent(event, xmlEventReader, eventWriter, bodyProcessingContext);
        }
    }

    @Override
    public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
        fallbackHandler.handleEndElementEvent(event, xmlEventReader, eventWriter);
    }

    protected String retrieveImageData(StartElement event, XMLEventReader xmlEventReader) {
        ElementRawDataParser rawDataParser = new ElementRawDataParser();
        try {
            return rawDataParser.parse(event.getName().getLocalPart(), xmlEventReader);
        } catch (XMLStreamException e) {
            return null;
        }
    }

    protected abstract String retrieveImageId(StartElement event);

    private String transformRawContentToStructuredFormat(String unprocessedContent, BodyProcessingContext bodyProcessingContext) {
        if (!StringUtils.isBlank(unprocessedContent)) {
            return transformingBodyProcessor.process(unprocessedContent, bodyProcessingContext);
        }
        return null;
    }

    protected abstract String getImageDataAttributeName();
}
