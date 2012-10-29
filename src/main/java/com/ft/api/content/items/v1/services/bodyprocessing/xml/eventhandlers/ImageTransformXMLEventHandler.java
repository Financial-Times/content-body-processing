package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.api.content.items.v1.services.bodyprocessing.ImageAttribute;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;


public class ImageTransformXMLEventHandler extends SimpleTransformTagXmlEventHandler {

	private final static String SRC_ATTRIBUTE = "fileref";
	private final static String UUIDPREFIX = "uuid=";
	private final static String ALT_ATTRIBUTE = "alt";
	private List<String> validAttributes = Arrays.asList("width", "height", "alt", "src");
	
	public ImageTransformXMLEventHandler(String newElement, String... attributesToAdd) {
		super(newElement, attributesToAdd);
	}
	
	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter, 
			BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		Map<String,String> attributesAndValues  = getValidAttributesAndValues(event, validAttributes);
		addDefaultAltAttributeIfMissing(attributesAndValues);
		attributesAndValues.putAll(getAttributesToAdd());
		try {
			String uuid = getUUIDForImage(event);
			overRideWithAttributesFromImage(attributesAndValues, uuid, bodyProcessingContext);
			eventWriter.writeStartTag(getNewElement(), attributesAndValues);
		} catch (BodyProcessingException e) {
			skipUntilMatchingEndTag(event.getName().getLocalPart(), xmlEventReader);
		}
	}

	private String getUUIDForImage(StartElement event) {
		Attribute fileref = event.getAttributeByName(new QName(SRC_ATTRIBUTE));
		if (fileref == null) {
			throw new BodyProcessingException("no attribute present for attribute name=" + SRC_ATTRIBUTE + " - required for getting uuid");
		}
		String filerefValue = fileref.getValue();
		int indexOfUuid= filerefValue.indexOf("uuid=");
		if (indexOfUuid == -1) {
			throw new BodyProcessingException("attribute" + SRC_ATTRIBUTE + " doesn't contain 'uuid='");
		}
		return filerefValue.substring(filerefValue.indexOf(UUIDPREFIX) + UUIDPREFIX.length());
	}

	
	private void overRideWithAttributesFromImage(Map<String, String> attributesAndValues, String uuid, 
			BodyProcessingContext bodyProcessingContext) {
		for(String attributeName: validAttributes) {
			ImageAttribute imageAttribute = ImageAttribute.getByName(attributeName);
		    String imageAttributeValue = bodyProcessingContext.getAttributeForImage(imageAttribute, uuid);
			if (imageAttributeValue != null) {
				attributesAndValues.put(attributeName, imageAttributeValue);
			}
		}
	}
	
	private void addDefaultAltAttributeIfMissing(Map<String, String> attributesAndValues) {
		if (!attributesAndValues.containsKey(ALT_ATTRIBUTE)) {
			attributesAndValues.put(ALT_ATTRIBUTE, "");
		}	
	}
}
