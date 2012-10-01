package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;

public class InteractiveGraphicXMLParser extends BaseXMLParser<InteractiveGraphicData> implements
        XmlParser<InteractiveGraphicData> {

    private static final String VALID_ID_PREFIX = "ig";
    private static final String SCRIPT_ATTRIBUTE_SRC = "src";
    private static final String SCRIPT_ELEMENT = "script";
    private static final String DIV_ATTRIBUTE_ID = "id";
    private static final String DIV_ELEMENT = "div";
    private static final String PLAIN_HTML_ELEMENT = "plainHtml";
    private static final String VALID_DATA_ACCESS_TYPE = "interactive-graphic";
    private final QName scriptDataAccessTypeAttribute = new QName("data-asset-type");

    protected InteractiveGraphicXMLParser() {
        super(PLAIN_HTML_ELEMENT);
    }
    
    @Override
    public void transformFieldContentToStructuredFormat(InteractiveGraphicData dataBean, BodyProcessingContext bodyProcessingContext) {
        //Do nothing as this is not supported/needed for Interactive Graphic elements
    }

    @Override
    protected InteractiveGraphicData createDataBeanInstance() {
        return new InteractiveGraphicData();
    }

    @Override
    void populateBeanUsingStartElement(InteractiveGraphicData interactiveGraphicData, StartElement nextStartElement,
            XMLEventReader xmlEventReader) {
        // look for a div or a script start element
        if (isElementNamed(nextStartElement.getName(), DIV_ELEMENT)) {
            String id = nextStartElement.getAttributeByName(new QName(DIV_ATTRIBUTE_ID)).getValue();
            interactiveGraphicData.setId(extractId(id));
        }
        if (isElementNamed(nextStartElement.getName(), SCRIPT_ELEMENT)) {
            if (isScriptCorrectType(nextStartElement)) {
                String src = nextStartElement.getAttributeByName(new QName(SCRIPT_ATTRIBUTE_SRC)).getValue();
                interactiveGraphicData.setSrc(extractSrc(src));
            }
        }
    }

    /**
     * Check whether the src contains a valid URL structure and return the input
     * value otherwise return null;
     * 
     * @param src
     * @return
     */
    private String extractSrc(String src) {
        try {
            new URL(src);
            // No exception thrown means that the URL is valid (according to java.net.URL)
            return src;
        } catch (MalformedURLException e) {
            // Not a valid URL. Don't parse the URL.
            return null;
        }
    }

    private boolean isScriptCorrectType(StartElement nextStartElement) {
        Attribute dataAccessType = nextStartElement.getAttributeByName(scriptDataAccessTypeAttribute);
        return dataAccessType.getValue().equals(VALID_DATA_ACCESS_TYPE);
    }

    private String extractId(String id) {
        if (id.startsWith(VALID_ID_PREFIX)) {
            return id.substring(2);
        }
        return null;
    }
}
