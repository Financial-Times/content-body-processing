package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import com.ft.content.bodyprocessing.BodyProcessingContext;

public class InteractiveGraphicXMLEventHandler extends AsideBaseXMLEventHandler<InteractiveGraphicData> {

    private static final String INTERACTIVE_GRAPHIC_ELEMENT_NAME = "plainHtml";
    private static final String INTERACTIVE_GRAPHIC_TYPE = "interactiveGraphic";
    
    private XmlParser<InteractiveGraphicData> interactiveGraphicXMLParser;

    public InteractiveGraphicXMLEventHandler(InteractiveGraphicXMLParser interactiveGraphicXMLParser, AsideElementWriter asideElementWriter) {
        super(asideElementWriter);
        notNull(interactiveGraphicXMLParser, "interactiveGraphicXMLParser cannot be null");
        this.interactiveGraphicXMLParser = interactiveGraphicXMLParser;
    }

    @Override
    String getElementName() {
        return INTERACTIVE_GRAPHIC_ELEMENT_NAME;
    }

    @Override
    String getType(InteractiveGraphicData dataType) {
        return INTERACTIVE_GRAPHIC_TYPE;
    }

    @Override
    void transformFieldContentToStructuredFormat(InteractiveGraphicData dataBean, BodyProcessingContext bodyProcessingContext) {
        interactiveGraphicXMLParser.transformFieldContentToStructuredFormat(dataBean, bodyProcessingContext);
    }

    @Override
    InteractiveGraphicData parseElementData(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException {
        return interactiveGraphicXMLParser.parseElementData(startElement, xmlEventReader);
    }
}
