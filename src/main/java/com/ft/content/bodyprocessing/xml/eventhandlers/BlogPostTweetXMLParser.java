package com.ft.content.bodyprocessing.xml.eventhandlers;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import com.ft.content.bodyprocessing.BodyProcessingContext;

public class BlogPostTweetXMLParser extends BaseXMLParser<TweetData> implements XmlParser<TweetData> {

    public BlogPostTweetXMLParser() {
        super("div");
    }

    @Override
    public void transformFieldContentToStructuredFormat(TweetData dataBean, BodyProcessingContext bodyProcessingContext) {
    }

    @Override
    boolean doesTriggerElementContainAllDataNeeded() {
        return false;
    }

    @Override
    TweetData createDataBeanInstance() {
        return new TweetData();
    }

    @Override
    void populateBean(TweetData tweetData, StartElement startElement, XMLEventReader xmlEventReader)
            throws UnexpectedElementStructureException {

        QName elementName = startElement.getName();
        if (isElementNamed(elementName, "div") && "tweet".equalsIgnoreCase(parseAttribute("data-asset-type", startElement))) {
            tweetData.setId(parseAttribute("data-asset-ref", startElement));
            tweetData.setTweetSource(parseAttribute("data-asset-source", startElement));
        }

    }

}
