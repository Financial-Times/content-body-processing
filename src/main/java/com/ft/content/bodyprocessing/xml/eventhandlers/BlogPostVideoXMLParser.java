package com.ft.content.bodyprocessing.xml.eventhandlers;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import com.ft.content.bodyprocessing.BodyProcessingContext;

public class BlogPostVideoXMLParser extends BaseXMLParser<VideoData> implements XmlParser<VideoData> {

    public BlogPostVideoXMLParser() {
        super("div");
    }

    @Override
    public void transformFieldContentToStructuredFormat(VideoData dataBean, BodyProcessingContext bodyProcessingContext) {
        // Do nothing as the only data is that of the video id and source. No need for additional body processing.
    }

    @Override
    boolean doesTriggerElementContainAllDataNeeded() {
        return false;
    }

    @Override
    VideoData createDataBeanInstance() {
        return new VideoData();
    }

    @Override
    void populateBean(VideoData videoData, StartElement startElement, XMLEventReader xmlEventReader)
            throws UnexpectedElementStructureException {

        QName elementName = startElement.getName();
        if (isElementNamed(elementName, "div") && "video".equalsIgnoreCase(parseAttribute("data-asset-type", startElement))) {
            videoData.setId(parseAttribute("data-asset-ref", startElement));
            videoData.setVideoSource(parseAttribute("data-asset-source", startElement));
        }

    }

}
