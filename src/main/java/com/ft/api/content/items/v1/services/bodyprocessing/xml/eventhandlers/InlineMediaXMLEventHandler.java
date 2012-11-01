package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.unifiedContentModel.model.Asset;

public class InlineMediaXMLEventHandler extends AsideBaseXMLEventHandler<SlideshowData> {

    private static final String ELEMENT_NAME = "inlineDwc";
    private static final String TYPE = "slideshow";
    private XmlParser<SlideshowData> slideshowXMLParser;
    private XMLEventHandler fallbackEventHandler;

    protected InlineMediaXMLEventHandler(XmlParser<SlideshowData> slideshowXMLParser, AsideElementWriter asideElementWriter, XMLEventHandler fallbackEventHandler) {
        super(asideElementWriter);
        
        notNull(slideshowXMLParser, "slideshowXMLParser cannot be null");
        notNull(fallbackEventHandler, "fallbackEventHandler cannot be null");
        
        this.slideshowXMLParser = slideshowXMLParser;
        this.fallbackEventHandler = fallbackEventHandler;
    }

    @Override
    String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    String getType() {
        return TYPE;
    }

    @Override
    void transformFieldContentToStructuredFormat(SlideshowData dataBean, BodyProcessingContext bodyProcessingContext) {
        // Do nothing, no need for further data processing.
    }
    
    @Override
    protected String addAssetToContextAndReturnAssetName(BodyProcessingContext bodyProcessingContext, SlideshowData dataBean) {
        Asset asset = bodyProcessingContext.assignAssetNameToExistingAsset(dataBean.getUuid());
        if(asset != null) {
            return asset.getName();
        } 
        return null;
    }

    @Override
    SlideshowData parseElementData(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException {
        return slideshowXMLParser.parseElementData(startElement, xmlEventReader);
    }
    
    @Override
    protected void processFallBack(StartElement startElement, XMLEventReader xmlEventReader, BodyWriter eventWriter,
            BodyProcessingContext bodyProcessingContext) throws BodyProcessingException, XMLStreamException {
        fallbackEventHandler.handleStartElementEvent(startElement, xmlEventReader, eventWriter, bodyProcessingContext);
    }
}
