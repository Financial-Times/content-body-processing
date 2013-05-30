package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.content.bodyprocessing.writer.BodyWriter;

public class BlogPostAssetXMLEventHandler<T extends AssetAware> extends AsideBaseXMLEventHandler<T> {

    private final XmlParser<T> assetXMLParser;
    private final XMLEventHandler fallbackHandler;
    private final String assetType;

    private static final QName ASSET_TYPE_ATTR = QName.valueOf("data-asset-type");
    private static final QName ASSET_SOURCE_ATTR = QName.valueOf("data-asset-source");
    private static final QName ASSET_REF_ATTR = QName.valueOf("data-asset-ref");

    private static final String ELEMENT_NAME = "div";

    protected BlogPostAssetXMLEventHandler(XmlParser<T> assetXMLParser, String assetType, AsideElementWriter asideElementWriter,
                                           XMLEventHandler fallbackHandler) {
        super(asideElementWriter);
        this.assetType = assetType;
        notNull(assetXMLParser, "assetXMLParser cannot be null");
        notNull(fallbackHandler, "fallbackHandler cannot be null");
        notNull(assetType, "assetType cannot be null");
        this.assetXMLParser = assetXMLParser;
        this.fallbackHandler = fallbackHandler;
    }

    @Override
    String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    String getType() {
        return assetType;
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
                attributeHasValue(event, ASSET_TYPE_ATTR, assetType);
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
    void transformFieldContentToStructuredFormat(T dataBean, BodyProcessingContext bodyProcessingContext) {
        // Do nothing since the only parsed data is a video id.
    }

    @Override
    T parseElementData(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException {
        return assetXMLParser.parseElementData(startElement, xmlEventReader);
    }

    @Override
    protected void processFallBack(StartElement startElement, XMLEventReader xmlEventReader, BodyWriter eventWriter,
                                   BodyProcessingContext bodyProcessingContext) throws BodyProcessingException, XMLStreamException {

        fallbackHandler.handleStartElementEvent(startElement, xmlEventReader, eventWriter, bodyProcessingContext);
    }

}
