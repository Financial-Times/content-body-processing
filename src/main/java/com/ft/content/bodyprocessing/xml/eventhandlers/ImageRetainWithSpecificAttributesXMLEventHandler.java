package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.ImageAttribute;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import java.util.Map;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;



public class ImageRetainWithSpecificAttributesXMLEventHandler extends RetainWithSpecificAttributesXMLEventHandler implements XMLEventHandler {

	private final static String ALT_ATTRIBUTE = ImageAttribute.ALT.getAttributeName();
	private final static String[] VALID_ATTRIBUTES = {ImageAttribute.SRC.getAttributeName(), ImageAttribute.ALT.getAttributeName(), 
            ImageAttribute.WIDTH.getAttributeName(), ImageAttribute.HEIGHT.getAttributeName(), ImageAttribute.IMAGE_ID.getAttributeName()};

	public ImageRetainWithSpecificAttributesXMLEventHandler(String... validAttributes) {
		super(VALID_ATTRIBUTES);
	}
	
	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter,
			BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		Map<String,String> validAttributesAndValues  = getValidAttributesAndValues(event, validAttributes);
		addDefaultAltAttributeIfMissing(validAttributesAndValues);
		addImageToProcessingContext(validAttributesAndValues, bodyProcessingContext);
        eventWriter.writeStartTag(event.getName().getLocalPart(), validAttributesAndValues);
	}

    protected void addImageToProcessingContext(Map<String, String> imageAttributes,
            BodyProcessingContext bodyProcessingContext) {
    }

    private void addDefaultAltAttributeIfMissing(Map<String, String> attributesAndValues) {
		if (!attributesAndValues.containsKey(ALT_ATTRIBUTE)) {
			attributesAndValues.put(ALT_ATTRIBUTE, "");
		}	
	}
}
