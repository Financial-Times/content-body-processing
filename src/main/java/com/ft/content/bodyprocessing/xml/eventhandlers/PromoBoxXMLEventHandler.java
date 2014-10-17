package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import com.ft.content.bodyprocessing.BodyProcessingContext;

public class PromoBoxXMLEventHandler extends AsideBaseXMLEventHandler<PromoBoxData> {


    private static final String PROMO_BOX_ELEMENT_NAME = "promo-box";
    private static final String PROMO_BOX_TYPE = "promoBox";
    private PromoBoxXMLParser promoBoxXMLParser;

    public PromoBoxXMLEventHandler(PromoBoxXMLParser promoBoxXMLParser, AsideElementWriter asideElementWriter) {
        super(asideElementWriter);
        notNull(promoBoxXMLParser, "promoBoxXMLParser cannot be null");
        this.promoBoxXMLParser = promoBoxXMLParser;
    }

    @Override
    String getElementName() {
        return PROMO_BOX_ELEMENT_NAME;
    }

    @Override
    String getType() {
        return PROMO_BOX_TYPE;
    }

    @Override
    void transformFieldContentToStructuredFormat(PromoBoxData dataBean, BodyProcessingContext bodyProcessingContext) {
        promoBoxXMLParser.transformFieldContentToStructuredFormat(dataBean, bodyProcessingContext);
    }

    @Override
    PromoBoxData parseElementData(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException {
        return promoBoxXMLParser.parseElementData(startElement, xmlEventReader);
    }
}