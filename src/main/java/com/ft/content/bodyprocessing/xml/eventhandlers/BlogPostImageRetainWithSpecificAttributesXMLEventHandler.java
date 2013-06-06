package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.api.ucm.model.v1.TypeBasedImage;
import com.ft.content.bodyprocessing.BodyProcessingContext;

import java.util.Map;

public class BlogPostImageRetainWithSpecificAttributesXMLEventHandler extends ImageRetainWithSpecificAttributesXMLEventHandler {

    @Override
    protected void addImageToProcessingContext(Map<String, String> imageAttributes,
            BodyProcessingContext bodyProcessingContext) {
        bodyProcessingContext.addImageWithAttributes(imageAttributes, TypeBasedImage.ImageType.INLINE_EXT);
    }
}
