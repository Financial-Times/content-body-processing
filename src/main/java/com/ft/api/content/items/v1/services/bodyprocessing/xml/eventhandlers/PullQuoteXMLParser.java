package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class PullQuoteXMLParser {

    private static final String QUOTE_SOURCE = "web-pull-quote-source";
    private static final String QUOTE_TEXT = "web-pull-quote-text";
    private static final String PULL_QUOTE = "web-pull-quote";
    private ElementRawDataParser rawDataParser = new ElementRawDataParser();

    public PullQuoteData parseElementData(XMLEventReader xmlEventReader) throws XMLStreamException {
        PullQuoteData pullQuoteData = new PullQuoteData();
        
        while (xmlEventReader.hasNext()) {
            XMLEvent nextEvent = xmlEventReader.nextEvent();
            
            if(nextEvent.isStartElement()) {
                StartElement nextStartElement = nextEvent.asStartElement();
                populateBeanWithStartElement(pullQuoteData, nextStartElement, xmlEventReader);
                
            } else if(nextEvent.isEndElement()) {
                // Check if it's the closing element of the web-pull-quote, in which case exit as we should not continue to parse beyond this element.
                if(isElementNamed(nextEvent.asEndElement().getName(), PULL_QUOTE)) {
                    break;
                }
            }
        }
        return pullQuoteData;
    }

    private void populateBeanWithStartElement(PullQuoteData pullQuoteData, StartElement nextStartElement, XMLEventReader xmlEventReader) throws XMLStreamException {
        // look for either web-pull-quote-text or web-pull-quote-source
        if(isElementNamed(nextStartElement.getName(), QUOTE_TEXT)) {
            pullQuoteData.setQuoteText(rawDataParser.parse(QUOTE_TEXT, xmlEventReader));
        }
        if(isElementNamed(nextStartElement.getName(), QUOTE_SOURCE)) {
            pullQuoteData.setQuoteSource(rawDataParser.parse(QUOTE_SOURCE, xmlEventReader));
        }
    }

    private boolean isElementNamed(QName elementName, String nameToMatch) {
        return elementName.getLocalPart().toLowerCase().equals(nameToMatch);
    }
}
