package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.apache.commons.lang.StringUtils;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.StAXTransformingBodyProcessor;

public class PullQuoteXMLParser extends BaseXMLParser<PullQuoteData> implements XmlParser<PullQuoteData> {

    private static final String QUOTE_SOURCE = "web-pull-quote-source";
    private static final String QUOTE_TEXT = "web-pull-quote-text";
    private static final String PULL_QUOTE = "web-pull-quote";
    private ElementRawDataParser rawDataParser;
    private StAXTransformingBodyProcessor stAXTransformingBodyProcessor;

    public PullQuoteXMLParser(StAXTransformingBodyProcessor stAXTransformingBodyProcessor) {
        super(PULL_QUOTE);
        notNull(stAXTransformingBodyProcessor, "The StAXTransformingBodyProcessor cannot be null.");
        this.stAXTransformingBodyProcessor = stAXTransformingBodyProcessor;
    }

    @Override
    public void transformFieldContentToStructuredFormat(PullQuoteData pullQuoteData, BodyProcessingContext bodyProcessingContext) {
        pullQuoteData.setQuoteText(transformRawContentToStructuredFormat(pullQuoteData.getQuoteText(), bodyProcessingContext));
        pullQuoteData.setQuoteSource(transformRawContentToStructuredFormat(pullQuoteData.getQuoteSource(), bodyProcessingContext));
    }

    @Override
    protected PullQuoteData createDataBeanInstance() {
        return new PullQuoteData();
    }

    private String transformRawContentToStructuredFormat(String unprocessedContent, BodyProcessingContext bodyProcessingContext) {
        if (!StringUtils.isBlank(unprocessedContent)) {
            return stAXTransformingBodyProcessor.process(unprocessedContent, bodyProcessingContext);
        }
        return null;
    }

    @Override
    protected void populateBean(PullQuoteData pullQuoteData, StartElement nextStartElement,
            XMLEventReader xmlEventReader) {
        // look for either web-pull-quote-text or web-pull-quote-source
        if (isElementNamed(nextStartElement.getName(), QUOTE_TEXT)) {
            pullQuoteData.setQuoteText(parseRawContent(QUOTE_TEXT, xmlEventReader));
        }
        if (isElementNamed(nextStartElement.getName(), QUOTE_SOURCE)) {
            pullQuoteData.setQuoteSource(parseRawContent(QUOTE_SOURCE, xmlEventReader));
        }
    }

    private String parseRawContent(String elementName, XMLEventReader xmlEventReader) {
        rawDataParser = new ElementRawDataParser();
        try {
            return rawDataParser.parse(elementName, xmlEventReader);
        } catch (XMLStreamException e) {
            return null;
        }
    }

}
