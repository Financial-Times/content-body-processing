package com.ft.content.bodyprocessing.xml.eventhandlers;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.ft.api.ucm.model.v1.Asset;

public class InlineMediaAssetXMLEventHandler extends AssetXMLEventHandler {

	private static final QName INLINE_DWC_TAG_QNAME = QName.valueOf("inlineDwc");
	private static final QName A_TAG_QNAME = QName.valueOf("a");

	public InlineMediaAssetXMLEventHandler(XMLEventHandler fallbackEventHandler) {
		super(fallbackEventHandler, INLINE_DWC_TAG_QNAME, new ExistingAssetManager());
	}

	@Override
	public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter bodyWriter,
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
					xmlEventReader.nextEvent();
					
					AsideElementWriter asideWriter = new AsideElementWriter();
					asideWriter.writeAsideElement(bodyWriter, asset.getName(), getAssetType());
					
					skipUntilMatchingEndTag(INLINE_DWC_TAG_QNAME.getLocalPart(), xmlEventReader);
					return;
				}
			}
		}
		fallbackEventHandler.handleStartElementEvent(event, xmlEventReader, bodyWriter, bodyProcessingContext);
	}

	@Override
	public boolean isAssetLink(StartElement event) {
		return event.getName().equals(INLINE_DWC_TAG_QNAME);
	}

	@Override
	public String getAssetType() {
		return "slideshow";
	}
}
