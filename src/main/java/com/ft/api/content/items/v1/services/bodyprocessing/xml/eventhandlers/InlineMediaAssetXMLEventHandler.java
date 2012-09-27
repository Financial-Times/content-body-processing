package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.unifiedContentModel.model.Asset;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class InlineMediaAssetXMLEventHandler extends AssetXMLEventHandler {

	private static final QName INLINE_DWC_TAG_QNAME = QName.valueOf("inlineDwc");
	private static final QName A_TAG_QNAME = QName.valueOf("a");

	public InlineMediaAssetXMLEventHandler(XMLEventHandler fallbackEventHandler) {
		super(fallbackEventHandler, INLINE_DWC_TAG_QNAME, new ExistingAssetManager());
	}

	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter,
			BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		
		if(!isValidElement(event)){
			throw new XMLStreamException("event must correspond to '"+INLINE_DWC_TAG_QNAME.getLocalPart()+"'");
		}
		
		XMLEvent next = xmlEventReader.peek();
		if(next instanceof StartElement){
			StartElement nextStart = next.asStartElement();
			QName nextName = nextStart.getName();
			if(nextName.equals(A_TAG_QNAME)){
				Asset asset = assetManager.createValidAttributesForMediaAsset(nextStart, bodyProcessingContext, getAssetType());

				if(asset != null){
					Map<String, String> validAttributes = new HashMap<String, String>();
					validAttributes.put(DATA_ASSET_TYPE_NAME, getAssetType());
					validAttributes.put(DATA_ASSET_NAME_NAME, asset.getName());

					xmlEventReader.nextEvent();
					eventWriter.writeEndTag(P_TAG_QNAME.getLocalPart());
					eventWriter.writeStartTag(ASIDE_TAG_QNAME.getLocalPart(), validAttributes);
					eventWriter.writeEndTag(ASIDE_TAG_QNAME.getLocalPart());
					eventWriter.writeStartTag(P_TAG_QNAME.getLocalPart(), null);
					skipUntilMatchingEndTag(INLINE_DWC_TAG_QNAME.getLocalPart(), xmlEventReader);
					return;
				}
			}
		}
		fallbackEventHandler.handleStartElementEvent(event, xmlEventReader, eventWriter, bodyProcessingContext);
	}

	@Override
	public boolean isAssetLink(StartElement event) {
		return event.getName().equals(INLINE_DWC_TAG_QNAME);
	}

	@Override
	public String getAssetType() {
		return "slideshow";
	}

	@Override
	protected boolean isInsidePTag() {
		return true;
	}
}
