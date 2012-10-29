package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;

public class VideoXMLParser extends BaseXMLParser<VideoData> implements XmlParser<VideoData> {


    private static final String VIDEO = "videoPlayer";
    private static final String VIDEO_ELEMENT = "videoPlayer";
    private static final String VIDEO_ID_ATTRIBUTE = "videoID";
    
    public VideoXMLParser() {
        super(VIDEO);
    }

    @Override
    public void transformFieldContentToStructuredFormat(VideoData dataBean, BodyProcessingContext bodyProcessingContext) {
        // Do nothing as the only data is that of the video id. No need for additional body processing.
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
        if (isElementNamed(elementName, VIDEO_ELEMENT)) {
            videoData.setId(parseAttribute(VIDEO_ID_ATTRIBUTE, startElement));
        }
        
    }

}
