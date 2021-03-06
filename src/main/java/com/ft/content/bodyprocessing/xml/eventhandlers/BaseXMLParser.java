package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;

public abstract class BaseXMLParser<T> {

    private String startElementName;

    protected BaseXMLParser(String startElementName) {
        notNull(startElementName, "The startElementName cannot be null!");
        this.startElementName = startElementName;
    }

    public T parseElementData(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException {
        T dataBean = createDataBeanInstance();

        try {
            // Use the start element (trigger element) to populate the bean as
            // some types require parsing to start from the starting element.
            populateBean(dataBean, startElement, xmlEventReader);

            // Check if more data beyond the start element is needed to populate the data bean
            if(doesTriggerElementContainAllDataNeeded()) {
                return dataBean;
            }
            int depth = 1;
            while (xmlEventReader.hasNext()) {
                XMLEvent nextEvent = xmlEventReader.nextEvent();

                if (nextEvent.isStartElement()) {
                    StartElement nextStartElement = nextEvent.asStartElement();
                    populateBean(dataBean, nextStartElement, xmlEventReader);
                    if(isElementNamed(nextStartElement.getName(), startElementName)) {
                        depth++;
                    }
                } else if (nextEvent.isEndElement()) {
                    // Check if it's the closing element of the start element,
                    // in which case exit as we should not continue to parse
                    // beyond this element.
                    if (isElementNamed(nextEvent.asEndElement().getName(), startElementName)) {
                        depth--;
                        if(depth==0) {
                            break;
                        }
                    }
                }
            }
        } catch (UnexpectedElementStructureException e) {
            // Something went wrong - read until the end of this element to get
            // rid of the whole thing
            skipUntilMatchingEndTag(startElementName, xmlEventReader);
            // Ensure that the bean returned is not valid for processing.
            return createDataBeanInstance();
        }
        return dataBean;
    }

    abstract boolean doesTriggerElementContainAllDataNeeded();

    abstract T createDataBeanInstance();

    protected boolean isElementNamed(QName elementName, String nameToMatch) {
        return elementName.getLocalPart().toLowerCase().equals(nameToMatch.toLowerCase());
    }

    abstract void populateBean(T dataBean, StartElement nextStartElement, XMLEventReader xmlEventReader)
            throws UnexpectedElementStructureException;

    private void skipUntilMatchingEndTag(String nameToMatch, XMLEventReader xmlEventReader) throws XMLStreamException {
        int count = 0;
        while (xmlEventReader.hasNext()) {
            XMLEvent event = xmlEventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement newStartElement = event.asStartElement();
                if (nameToMatch.equals(newStartElement.getName().getLocalPart())) {
                    count++;
                }
            }
            if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                String localName = endElement.getName().getLocalPart();
                if (nameToMatch.equals(localName)) {
                    if (count == 0) {
                        return;
                    }
                    count--;
                }
            }
        }
    }

    protected String parseRawContent(String elementName, XMLEventReader xmlEventReader) {
        ElementRawDataParser rawDataParser = new ElementRawDataParser();
        try {
            return rawDataParser.parse(elementName, xmlEventReader);
        } catch (XMLStreamException e) {
            return null;
        }
    }

    protected String parseRawContent(String elementName, XMLEventReader xmlEventReader, StartElement nextStartElement) {
        ElementRawDataParser rawDataParser = new ElementRawDataParser();
        try {
            return rawDataParser.parse(elementName, xmlEventReader, nextStartElement);
        } catch (XMLStreamException e) {
            return null;
        }
    }

    protected String parseAttribute(String attributeName, StartElement startElement) {
      Attribute attributeValue = startElement.getAttributeByName(new QName(attributeName));
      if(attributeValue == null || StringUtils.isBlank(attributeValue.getValue())) {
          return null;
      }
      return attributeValue.getValue();
    }
}
