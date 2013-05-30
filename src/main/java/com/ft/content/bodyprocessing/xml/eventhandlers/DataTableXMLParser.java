package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import org.apache.commons.lang.StringUtils;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.xml.StAXTransformingBodyProcessor;

public class DataTableXMLParser extends BaseXMLParser<DataTableData> implements XmlParser<DataTableData> {

	private static final String START_ELEMENT_NAME = "table";
    private StAXTransformingBodyProcessor stAXTransformingBodyProcessor;

	public DataTableXMLParser(StAXTransformingBodyProcessor stAXTransformingBodyProcessor) {
		super(START_ELEMENT_NAME);
				
		notNull(stAXTransformingBodyProcessor, "The StAXTransformingBodyProcessor cannot be null.");
        this.stAXTransformingBodyProcessor = stAXTransformingBodyProcessor;
	}

	@Override
	DataTableData createDataBeanInstance() {
		return new DataTableData();
	}

	@Override
	void populateBean(DataTableData dataTableData, StartElement nextStartElement, XMLEventReader xmlEventReader) throws UnexpectedElementStructureException {
		if (isElementNamed(nextStartElement.getName(), START_ELEMENT_NAME)) {
			dataTableData.setBody(parseRawContent(START_ELEMENT_NAME, xmlEventReader, nextStartElement));
		}
	}

	@Override
	public void transformFieldContentToStructuredFormat(DataTableData dataTableData, BodyProcessingContext bodyProcessingContext) {
		dataTableData.setBody(transformRawContentToStructuredFormat(dataTableData.getBody(), bodyProcessingContext));
	}
	
    @Override
    boolean doesTriggerElementContainAllDataNeeded() {
        return true;
    }

	private String transformRawContentToStructuredFormat(String unprocessedContent, BodyProcessingContext bodyProcessingContext) {
		if (!StringUtils.isBlank(unprocessedContent)) {
			return stAXTransformingBodyProcessor.process(unprocessedContent, bodyProcessingContext);
		}
		return null;
	}
 }
