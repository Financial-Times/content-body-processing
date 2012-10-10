package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.StAXTransformingBodyProcessor;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import org.apache.commons.lang.StringUtils;

public class DataTableXMLParser extends BaseXMLParser<DataTableData> implements XmlParser<DataTableData> {

	private StAXTransformingBodyProcessor stAXTransformingBodyProcessor;
	private ElementRawDataParser rawDataParser = new ElementRawDataParser();

	public DataTableXMLParser(StAXTransformingBodyProcessor stAXTransformingBodyProcessor) {
		super("web-table");
				
		notNull(stAXTransformingBodyProcessor, "The StAXTransformingBodyProcessor cannot be null.");
        this.stAXTransformingBodyProcessor = stAXTransformingBodyProcessor;
	}

	@Override
	DataTableData createDataBeanInstance() {
		return new DataTableData();
	}

	@Override
	void populateBean(DataTableData dataTableData, StartElement nextStartElement, XMLEventReader xmlEventReader) throws UnexpectedElementStructureException {
		if (isElementNamed(nextStartElement.getName(), "table")) {
			dataTableData.setBody(parseRawContent("table", xmlEventReader, nextStartElement));
		}
	}

	@Override
	public void transformFieldContentToStructuredFormat(DataTableData dataTableData, BodyProcessingContext bodyProcessingContext) {
		dataTableData.setBody(transformRawContentToStructuredFormat(dataTableData.getBody(), bodyProcessingContext));
	}

	private String transformRawContentToStructuredFormat(String unprocessedContent, BodyProcessingContext bodyProcessingContext) {
		if (!StringUtils.isBlank(unprocessedContent)) {
			return stAXTransformingBodyProcessor.process(unprocessedContent, bodyProcessingContext);
		}
		return null;
	}

	private String parseRawContent(String elementName, XMLEventReader xmlEventReader, StartElement nextStartElement) {
     try {
         return rawDataParser.parse(elementName, xmlEventReader, nextStartElement);
     } catch (XMLStreamException e) {
         return null;
     }
 }

}
