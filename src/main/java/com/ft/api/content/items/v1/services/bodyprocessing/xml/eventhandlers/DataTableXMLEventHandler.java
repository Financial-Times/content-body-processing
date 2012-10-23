package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

public class DataTableXMLEventHandler extends AsideBaseXMLEventHandler<DataTableData> {

	private XmlParser<DataTableData> dataTableDataXmlParser;
	private StripElementAndContentsXMLEventHandler stripElementAndContentsXMLEventHandler;

	public DataTableXMLEventHandler(XmlParser<DataTableData> dataTableDataXmlParser, AsideElementWriter asideElementWriter, 
	                                StripElementAndContentsXMLEventHandler stripElementAndContentsXMLEventHandler){
		super(asideElementWriter);
		notNull(dataTableDataXmlParser, "dataTableDataXmlParser cannot be null");
		notNull(stripElementAndContentsXMLEventHandler, "stripElementAndContentsXMLEventHandler cannot be null");
		
		this.dataTableDataXmlParser = dataTableDataXmlParser;
		this.stripElementAndContentsXMLEventHandler = stripElementAndContentsXMLEventHandler;
	}

	@Override
	String getElementName() {
		return "table";
	}

	@Override
	String getType() {
		return "dataTable";
	}

	@Override
	void transformFieldContentToStructuredFormat(DataTableData dataBean, BodyProcessingContext bodyProcessingContext) {
		dataTableDataXmlParser.transformFieldContentToStructuredFormat(dataBean, bodyProcessingContext);
	}

	@Override
	DataTableData parseElementData(StartElement startElement,XMLEventReader xmlEventReader) throws XMLStreamException {
		return dataTableDataXmlParser.parseElementData(startElement, xmlEventReader);
	}

	protected boolean isElementOfCorrectType(StartElement event) {
		if(event.getName().getLocalPart().toLowerCase().equals(getElementName().toLowerCase())){
			Attribute classAttr =  event.getAttributeByName(QName.valueOf("class"));
			if(classAttr != null && classAttr.getValue().toLowerCase().equals("data-table")){
				return true;
			}
		}
		return false;
	}

	@Override
	protected void processFallBack(StartElement startElement, XMLEventReader xmlEventReader, BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext) throws BodyProcessingException, XMLStreamException {
		stripElementAndContentsXMLEventHandler.handleStartElementEvent(startElement, xmlEventReader, eventWriter, bodyProcessingContext);
	}
}
