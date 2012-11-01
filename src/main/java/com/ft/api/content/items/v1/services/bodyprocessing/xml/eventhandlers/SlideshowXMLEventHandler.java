package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.unifiedContentModel.model.Asset;

public class SlideshowXMLEventHandler extends AsideBaseXMLEventHandler<SlideshowData> {

    private static final String ELEMENT_NAME = "a";
    private static final String TYPE_SLIDESHOW = "slideshow";
    private XMLEventHandler fallbackEventHandler;
    private XmlParser<SlideshowData> slideshowXMLParser;
    private static final String TYPE_ATTRIBUTE_NAME = "type";

    protected SlideshowXMLEventHandler(XmlParser<SlideshowData> slideshowXMLParser, AsideElementWriter asideElementWriter, XMLEventHandler fallbackEventHandler) {
        super(asideElementWriter);
        
        notNull(fallbackEventHandler, "fallbackEventHandler cannot be null");
        notNull(slideshowXMLParser, "slideshowXMLParser cannot be null");
        
        this.fallbackEventHandler = fallbackEventHandler;
        this.slideshowXMLParser = slideshowXMLParser;
    }

    @Override
    String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    String getType() {
        return TYPE_SLIDESHOW;
    }

    @Override
    void transformFieldContentToStructuredFormat(SlideshowData dataBean, BodyProcessingContext bodyProcessingContext) {
        // Do nothing, no need for further data processing.
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

    @Override
    protected String addAssetToContextAndReturnAssetName(BodyProcessingContext bodyProcessingContext, SlideshowData dataBean) {
        Asset asset = bodyProcessingContext.assignAssetNameToExistingAsset(dataBean.getUuid());
        if(asset != null) {
            return asset.getName();
        } 
        return null;
    }
    
    @Override
    protected boolean isElementOfCorrectType(StartElement event) {
        Attribute typeAttribute = event.getAttributeByName(QName.valueOf(TYPE_ATTRIBUTE_NAME));
        return typeAttribute != null && TYPE_SLIDESHOW.equals(typeAttribute.getValue());
    }
}
