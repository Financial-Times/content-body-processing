package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessor;
import com.ft.content.bodyprocessing.ImageAttribute;

import javax.xml.stream.events.StartElement;

public class BlogPostInlineImageSourceEventHandler extends BlogPostInlineImageDataEventHandler {
    private static final String SOURCE_TAG = "span";

    public BlogPostInlineImageSourceEventHandler(XMLEventHandler fallbackHandler, BodyProcessor transformingBodyProcessor) {
        super(fallbackHandler, transformingBodyProcessor);
    }

    protected String getImageDataAttributeName() {
        return ImageAttribute.SOURCE.getAttributeName();
    }

    @Override
    protected String retrieveImageId(StartElement event) {
        if (SOURCE_TAG.equals(event.getName().getLocalPart())) {
            return getValidAttributesAndValues(event).get(ImageAttribute.IMAGE_ID.getAttributeName());
        }
        return null;
    }

}
