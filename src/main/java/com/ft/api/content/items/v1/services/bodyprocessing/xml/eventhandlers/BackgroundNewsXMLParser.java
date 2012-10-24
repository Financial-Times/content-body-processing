package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.StAXTransformingBodyProcessor;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import org.apache.commons.lang.StringUtils;

public class BackgroundNewsXMLParser extends BaseXMLParser<BackgroundNewsData> implements XmlParser<BackgroundNewsData> {

	private static final String WEB_BACKGROUND_NEWS_TEXT = "web-background-news-text";
	private static final String WEB_BACKGROUND_NEWS_HEADER = "web-background-news-header";
	private static final String WEB_BACKGROUND_NEWS = "web-background-news";
	private StAXTransformingBodyProcessor stAXTransformingBodyProcessor;

	public BackgroundNewsXMLParser(StAXTransformingBodyProcessor stAXTransformingBodyProcessor) {
		 super(WEB_BACKGROUND_NEWS);
		 notNull(stAXTransformingBodyProcessor, "The StAXTransformingBodyProcessor cannot be null.");
		 this.stAXTransformingBodyProcessor = stAXTransformingBodyProcessor;
	}

	@Override
	BackgroundNewsData createDataBeanInstance() {
		return new BackgroundNewsData();
	}

	@Override
	protected void populateBean(BackgroundNewsData backgroundNewsData, StartElement nextStartElement, XMLEventReader xmlEventReader) throws UnexpectedElementStructureException {
        if (isElementNamed(nextStartElement.getName(), WEB_BACKGROUND_NEWS_TEXT)) {
            backgroundNewsData.setText(parseRawContent(WEB_BACKGROUND_NEWS_TEXT, xmlEventReader));
        }
        if (isElementNamed(nextStartElement.getName(), WEB_BACKGROUND_NEWS_HEADER)) {
			backgroundNewsData.setHeader(parseRawContent(WEB_BACKGROUND_NEWS_HEADER, xmlEventReader));
        }
	}

	@Override
	public void transformFieldContentToStructuredFormat(BackgroundNewsData backgroundNewsData, BodyProcessingContext bodyProcessingContext) {
		backgroundNewsData.setText(transformRawContentToStructuredFormat(backgroundNewsData.getText(), bodyProcessingContext));
		backgroundNewsData.setHeader(transformRawContentToStructuredFormat(backgroundNewsData.getHeader(), bodyProcessingContext));
	}

	private String transformRawContentToStructuredFormat(String unprocessedContent, BodyProcessingContext bodyProcessingContext) {
		if (!StringUtils.isBlank(unprocessedContent)) {
			return stAXTransformingBodyProcessor.process(unprocessedContent, bodyProcessingContext);
		}
		return null;
	}

    @Override
    boolean doesTriggerElementContainAllDataNeeded() {
        return false;
    }
}
