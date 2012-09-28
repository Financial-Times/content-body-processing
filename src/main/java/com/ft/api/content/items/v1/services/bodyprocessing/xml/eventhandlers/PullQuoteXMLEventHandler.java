package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.StAXTransformingBodyProcessor;
import com.ft.unifiedContentModel.model.Asset;
import com.ft.unifiedContentModel.model.PullQuote;
import com.ft.unifiedContentModel.model.PullQuoteFields;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import org.apache.commons.lang.StringUtils;

public class PullQuoteXMLEventHandler extends BaseXMLEventHandler {

    private static final String WEB_PULL_QUOTE_ELEMENT_NAME = "web-pull-quote";
    private static final String WEB_PULL_QUOTE_TYPE = "pullquote";

    private PullQuoteXMLParser pullQuoteXMLParser;
    private AsideElementWriter asideElementWriter;
    private StAXTransformingBodyProcessor stAXTransformingBodyProcessor;

    public PullQuoteXMLEventHandler(PullQuoteXMLParser pullQuoteXMLParser,
            AsideElementWriter asideElementWriter,
            StAXTransformingBodyProcessor stAXTransformingBodyProcessor) {
        
        notNull(pullQuoteXMLParser, "pullQuoteXMLParser cannot be null");
        notNull(asideElementWriter, "asideElementWriter cannot be null");
        notNull(stAXTransformingBodyProcessor, "stAXTransformingBodyProcessor cannot be null");
        
        this.pullQuoteXMLParser = pullQuoteXMLParser;
        this.asideElementWriter = asideElementWriter;
        this.stAXTransformingBodyProcessor = stAXTransformingBodyProcessor;
    }

    @Override
    public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter,
            BodyProcessingContext bodyProcessingContext) throws XMLStreamException {

        // Confirm that the startEvent is a pull quote
        if (isAssetWebPullQuote(event)) {

            // Parse the pull quote data
            PullQuoteData pullQuoteData = pullQuoteXMLParser.parseElementData(xmlEventReader);

            // Process parsed content, since it was parsed 'as is' and might
            // have invalid mark-up
            processParsedContent(pullQuoteData, bodyProcessingContext);

            // Create asset and aside element if all data is parsed
            if (pullQuoteData.isOkToRender()) {
                // create the asset by delegating to the PullQuoteManager
                Asset asset = buildAsset(bodyProcessingContext, pullQuoteData);

                // build aside element and skip until end of tag
                asideElementWriter.writeAsideElement(eventWriter, asset.getName(), WEB_PULL_QUOTE_TYPE, true);
            }
        } else {
            throw new XMLStreamException("event must correspond to" + WEB_PULL_QUOTE_ELEMENT_NAME + " tag");
        }

    }

    private Asset buildAsset(BodyProcessingContext bodyProcessingContext, PullQuoteData pullQuoteData) {
        PullQuoteFields pullQuoteDataFields = new PullQuoteFields(pullQuoteData.getQuoteText(), pullQuoteData.getQuoteSource());
        PullQuote pullQuoteAsset = new PullQuote(pullQuoteDataFields);
        return bodyProcessingContext.addAsset(pullQuoteAsset);
    }
    
    private void processParsedContent(PullQuoteData pullQuoteData, BodyProcessingContext bodyProcessingContext) {
        pullQuoteData.setQuoteText(processRawContent(pullQuoteData.getQuoteText(), bodyProcessingContext));
        pullQuoteData.setQuoteSource(processRawContent(pullQuoteData.getQuoteSource(), bodyProcessingContext));
    }

    private String processRawContent(String unprocessedContent, BodyProcessingContext bodyProcessingContext) {
        if (!StringUtils.isBlank(unprocessedContent)) {
            return stAXTransformingBodyProcessor.process(unprocessedContent, bodyProcessingContext);
        }
        return null;
    }

    public boolean isAssetWebPullQuote(StartElement event) {
        return event.getName().getLocalPart().toLowerCase().equals(WEB_PULL_QUOTE_ELEMENT_NAME);
    }
}
