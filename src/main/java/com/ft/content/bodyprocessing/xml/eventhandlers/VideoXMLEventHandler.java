package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.content.bodyprocessing.writer.BodyWriter;

public class VideoXMLEventHandler extends AsideBaseXMLEventHandler<VideoData> {

    private VideoXMLParser videoXMLParser;
    private StripElementAndContentsXMLEventHandler stripElementAndContentsXMLEventHandler;
    private static final String VIDEO_TYPE = "video";
    private static final String ELEMENT_NAME = "videoPlayer";

    protected VideoXMLEventHandler(VideoXMLParser videoXMLParser, AsideElementWriter asideElementWriter, 
                                        StripElementAndContentsXMLEventHandler stripElementAndContentsXMLEventHandler) {
        super(asideElementWriter);
        notNull(videoXMLParser, "videoXMLParser cannot be null");
        notNull(stripElementAndContentsXMLEventHandler, "stripElementAndContentsXMLEventHandler cannot be null");
        this.videoXMLParser = videoXMLParser;
        this.stripElementAndContentsXMLEventHandler = stripElementAndContentsXMLEventHandler;
    }

    @Override
    String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    String getType() {
        return VIDEO_TYPE;
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

        stripElementAndContentsXMLEventHandler.handleStartElementEvent(startElement, xmlEventReader, eventWriter, bodyProcessingContext);
    }
}
