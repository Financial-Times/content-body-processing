package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import java.util.Map;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;



public class ImageRetainWithSpecificAttributesXMLEventHandler extends RetainWithSpecificAttributesXMLEventHandler implements XMLEventHandler {

	private final static String ALT_ATTRIBUTE = "alt";
	private final static String[] VALID_ATTRIBUTES = {"src", "alt", "width", "height"};

	public ImageRetainWithSpecificAttributesXMLEventHandler(String... validAttributes) {
		super(VALID_ATTRIBUTES);
	}
	
	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter,
			BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		Map<String,String> validAttributesAndValues  = getValidAttributesAndValues(event, validAttributes);
		addDefaultAltAttributeIfMissing(validAttributesAndValues);
		eventWriter.writeStartTag(event.getName().getLocalPart(), validAttributesAndValues);
	}
	
	private void addDefaultAltAttributeIfMissing(Map<String, String> attributesAndValues) {
		if (!attributesAndValues.containsKey(ALT_ATTRIBUTE)) {
			attributesAndValues.put(ALT_ATTRIBUTE, "");
		}	
	}
}
