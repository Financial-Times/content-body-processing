package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.unifiedContentModel.model.Asset;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public abstract class AssetXMLEventHandler extends BaseXMLEventHandler implements AssetXMLEventHandlable {

	protected final QName START_TAG_QNAME;
	protected static final QName ASIDE_TAG_QNAME = QName.valueOf("aside");
	protected static final QName P_TAG_QNAME = QName.valueOf("p");
	protected static final String DATA_ASSET_NAME_NAME = "data-asset-name";
	protected static final String DATA_ASSET_TYPE_NAME = "data-asset-type";

	protected XMLEventHandler fallbackEventHandler;
	protected AssetManager assetManager;

	public AssetXMLEventHandler(XMLEventHandler fallbackEventHandler, QName startTag, AssetManager assetManager) {
		this.fallbackEventHandler = fallbackEventHandler;
		START_TAG_QNAME = startTag;
		this.assetManager = assetManager;
	}

	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
		if(!isValidElement(event)){
			throw new XMLStreamException("event must correspond to" + START_TAG_QNAME.toString()  +" tag");
		}

		if(isAssetLink(event) ){
			Asset asset = assetManager.createValidAttributesForMediaAsset(event, bodyProcessingContext, getAssetType());
			if(asset != null){
				Map<String, String> validAttributes = new HashMap<String, String>();
				validAttributes.put(DATA_ASSET_NAME_NAME, asset.getName());
				validAttributes.put(DATA_ASSET_TYPE_NAME, getAssetType());

				if (isInsidePTag()) {
					closePTag(eventWriter);
					writeAside(eventWriter, validAttributes);
					openPTag(eventWriter);
					
				} else {
					writeAside(eventWriter, validAttributes);
				}

 				skipUntilMatchingEndTag(START_TAG_QNAME.getLocalPart(), xmlEventReader);
				return;
			}
		}

		fallbackEventHandler.handleStartElementEvent(event, xmlEventReader, eventWriter, bodyProcessingContext);
	}

	private void closePTag(BodyWriter eventWriter) {
		eventWriter.writeEndTag(P_TAG_QNAME.getLocalPart());
	}

	private void writeAside(BodyWriter eventWriter, Map<String, String> validAttributes) {
		eventWriter.writeStartTag(ASIDE_TAG_QNAME.getLocalPart(), validAttributes);
		eventWriter.writeEndTag(ASIDE_TAG_QNAME.getLocalPart());
	}

	private void openPTag(BodyWriter eventWriter) {
		eventWriter.writeStartTag(P_TAG_QNAME.getLocalPart(), null);
	}

	// whether this type of asset is found inside a p tag or not
	protected abstract boolean isInsidePTag(); 

	@Override
	public void handleEndElementEvent(EndElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter) throws XMLStreamException {
		if(!event.getName().equals(START_TAG_QNAME)){
			throw new XMLStreamException("event must correspond to" + START_TAG_QNAME.toString()  + " tag");
		}
		fallbackEventHandler.handleEndElementEvent(event, xmlEventReader, eventWriter);
	}


	@Override
	public boolean isValidElement(StartElement event) {
		return event.getName().equals(START_TAG_QNAME);
	}

}
