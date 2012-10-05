package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

public class BackgroundNewsXMLEventHandler extends AsideBaseXMLEventHandler<BackgroundNewsData> {

	private static final String WEB_BACKGROUND_NEWS_ELEMENT_NAME = "web-background-news";
	private static final String WEB_BACKGROUND_NEWS_TYPE = "backgroundNews";
	private XmlParser<BackgroundNewsData> backgroundNewsDataXmlParser;

	public BackgroundNewsXMLEventHandler(XmlParser<BackgroundNewsData> backgroundNewsDataXmlParser, AsideElementWriter asideElementWriter) {
		super(asideElementWriter);
		notNull(backgroundNewsDataXmlParser, "backgroundNewsDataXmlParser cannot be null");

	    this.backgroundNewsDataXmlParser = backgroundNewsDataXmlParser;
	}

	@Override
	boolean needPTag() {
		return true;
	}

	@Override
	String getElementName() {
		return WEB_BACKGROUND_NEWS_ELEMENT_NAME;
	}

	@Override
	String getType() {
		return WEB_BACKGROUND_NEWS_TYPE;
	}

	@Override
	void transformFieldContentToStructuredFormat(BackgroundNewsData dataBean, BodyProcessingContext bodyProcessingContext) {
		backgroundNewsDataXmlParser.transformFieldContentToStructuredFormat(dataBean, bodyProcessingContext);
	}

	@Override
	BackgroundNewsData parseElementData(XMLEventReader xmlEventReader) throws XMLStreamException {
		return backgroundNewsDataXmlParser.parseElementData(xmlEventReader);
	}
}
