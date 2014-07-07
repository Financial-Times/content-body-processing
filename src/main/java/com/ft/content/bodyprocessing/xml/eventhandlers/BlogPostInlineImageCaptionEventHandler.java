package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.ImageAttribute;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public class BlogPostInlineImageCaptionEventHandler extends BaseXMLEventHandler {

    private static final String CAPTION_TAG = "p";
    private XMLEventHandler fallbackHandler;

    public BlogPostInlineImageCaptionEventHandler(XMLEventHandler fallbackHandler) {
        this.fallbackHandler = fallbackHandler;
    }

    @Override
    public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext)
            throws XMLStreamException {
        String imageId = retrieveImageId(event);
        if (imageId != null) {
            String caption = retrieveCaption(event, xmlEventReader);
            if (StringUtils.isNotEmpty(caption)) {
                bodyProcessingContext.addAttributesToExistingImageWithId(imageId, ImmutableMap.of(ImageAttribute.CAPTION.getAttributeName(), caption));
            }
        } else {
            fallbackHandler.handleStartElementEvent(event, xmlEventReader, eventWriter, bodyProcessingContext);
        }
    }

    @Override
    public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
        fallbackHandler.handleEndElementEvent(event, xmlEventReader, eventWriter);
    }

    private String retrieveCaption(StartElement event, XMLEventReader xmlEventReader) {
        ElementRawDataParser rawDataParser = new ElementRawDataParser();
        try {
            return rawDataParser.parse(event.getName().getLocalPart(), xmlEventReader);
        } catch (XMLStreamException e) {
            return null;
        }
    }

    private String retrieveImageId(StartElement event) {
        if (CAPTION_TAG.equals(event.getName().getLocalPart())) {
            return getValidAttributesAndValues(event).get(ImageAttribute.IMAGE_ID.getAttributeName());
        }
        return null;
    }


}
