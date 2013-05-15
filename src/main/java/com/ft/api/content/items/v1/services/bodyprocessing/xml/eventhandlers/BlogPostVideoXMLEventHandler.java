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

public class BlogPostVideoXMLEventHandler extends AsideBaseXMLEventHandler<VideoData> {

    private final BlogPostVideoXMLParser videoXMLParser;
    private final XMLEventHandler fallbackHandler;

    private static final QName ASSET_TYPE_ATTR = QName.valueOf("data-asset-type");
    private static final QName ASSET_SOURCE_ATTR = QName.valueOf("data-asset-source");
    private static final QName ASSET_REF_ATTR = QName.valueOf("data-asset-ref");

    private static final String ELEMENT_NAME = "div";
    private static final String ASSET_TYPE = "video";

    protected BlogPostVideoXMLEventHandler(BlogPostVideoXMLParser blogPostVideoXMLParser, AsideElementWriter asideElementWriter,
                                           XMLEventHandler fallbackHandler) {
        super(asideElementWriter);
        notNull(blogPostVideoXMLParser, "blogPostVideoXMLParser cannot be null");
        notNull(fallbackHandler, "fallbackHandler cannot be null");
        this.videoXMLParser = blogPostVideoXMLParser;
        this.fallbackHandler = fallbackHandler;
    }

    @Override
    String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    String getType() {
        return ASSET_TYPE;
    }

    @Override
    protected boolean isElementOfCorrectType(StartElement event) {
        return super.isElementOfCorrectType(event) &&
                elementHasCorrectAttributes(event);
    }

    private boolean elementHasCorrectAttributes(StartElement event) {
        return attributeIsPopulated(event, ASSET_TYPE_ATTR) &&
                attributeIsPopulated(event, ASSET_SOURCE_ATTR) &&
                attributeIsPopulated(event, ASSET_REF_ATTR) &&
                attributeHasValue(event, ASSET_TYPE_ATTR, "video");
    }

    private boolean attributeHasValue(StartElement element, QName attrName, String expectedValue) {
        Attribute attr = element.getAttributeByName(attrName);
        return expectedValue.equalsIgnoreCase(attr.getValue());
    }

    private boolean attributeIsPopulated(StartElement element, QName qname) {
        Attribute attr = element.getAttributeByName(qname);
        return attr != null && !isEmpty(attr.getValue());
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().equals("");
    }
    @Override
    void transformFieldContentToStructuredFormat(VideoData dataBean, BodyProcessingContext bodyProcessingContext) {
        // Do nothing since the only parsed data is a video id.
    }

    @Override
    VideoData parseElementData(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException {
        return this.videoXMLParser.parseElementData(startElement, xmlEventReader);
    }

    @Override
    protected void processFallBack(StartElement startElement, XMLEventReader xmlEventReader, BodyWriter eventWriter,
                                   BodyProcessingContext bodyProcessingContext) throws BodyProcessingException, XMLStreamException {

        fallbackHandler.handleStartElementEvent(startElement, xmlEventReader, eventWriter, bodyProcessingContext);
    }

}
