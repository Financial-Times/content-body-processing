package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessor;
import com.ft.content.bodyprocessing.ImageAttribute;

import javax.xml.stream.events.StartElement;

public class BlogPostInlineImageCaptionEventHandler extends BlogPostInlineImageDataEventHandler {

    private static final String CAPTION_TAG = "p";

    public BlogPostInlineImageCaptionEventHandler(XMLEventHandler fallbackHandler, BodyProcessor transformingBodyProcessor) {
        super(fallbackHandler, transformingBodyProcessor);
    }

    @Override
    protected String retrieveImageId(StartElement event) {
        if (CAPTION_TAG.equals(event.getName().getLocalPart())) {
            return getValidAttributesAndValues(event).get(ImageAttribute.IMAGE_ID.getAttributeName());
        }
        return null;
    }

    @Override
    protected String getImageDataAttributeName() {
        return ImageAttribute.CAPTION.getAttributeName();
    }

}
