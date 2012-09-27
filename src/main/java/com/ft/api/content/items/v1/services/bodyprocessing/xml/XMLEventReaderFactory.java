package com.ft.api.content.items.v1.services.bodyprocessing.xml;

import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamProperties;

public class XMLEventReaderFactory {

    private static final String HTML_ELEMENT_START = "<html>";
    private static final String HTML_ELEMENT_END = "</html>";
    
    private XMLInputFactory2 xmlInputFactory;

    public XMLEventReaderFactory(XMLInputFactory2 xmlInputFactory) {
        this.xmlInputFactory = xmlInputFactory;
        this.xmlInputFactory.setProperty(XMLStreamProperties.XSP_NAMESPACE_AWARE, false);
    }
    
    public XMLEventReader createXMLEventReader(String xml) throws XMLStreamException {
        String paddedXml = padXmlWithRootNode(xml);
        StringReader stringReader = new StringReader(paddedXml);
        return  xmlInputFactory.createXMLEventReader(stringReader);
    }

    private String padXmlWithRootNode(String xml) {
        return HTML_ELEMENT_START.concat(xml).concat(HTML_ELEMENT_END);
    }

}
