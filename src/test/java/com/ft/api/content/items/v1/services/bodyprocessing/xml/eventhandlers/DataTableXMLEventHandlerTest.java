package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.StAXTransformingBodyProcessor;
import com.ft.unifiedContentModel.model.Asset;

@RunWith(MockitoJUnitRunner.class)
public class DataTableXMLEventHandlerTest extends BaseXMLEventHandlerTest  {

    @Mock private BodyWriter mockEventWriter;
    @Mock private XMLEventReader2 mockXmlEventReader;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private Asset mockAsset;
    @Mock private DataTableXMLParser mockDataTableXMLParser;
    @Mock private AsideElementWriter mockAsideElementWriter;
    @Mock private DataTableData mockDataTableData;
    @Mock private StAXTransformingBodyProcessor mockStAXTransformingBodyProcessor;
    @Mock private StripElementAndContentsXMLEventHandler mockStripElementAndContentsXMLEventHandler;

    private StartElement startElement;
    private BaseXMLEventHandler dataTableXMLEventHandler;


    @Before
    public void setup() throws XMLStreamException {
      dataTableXMLEventHandler = new DataTableXMLEventHandler(mockDataTableXMLParser, mockAsideElementWriter, mockStripElementAndContentsXMLEventHandler);
      when(mockDataTableXMLParser.parseElementData(Mockito.any(StartElement.class), Mockito.eq(mockXmlEventReader))).thenReturn(mockDataTableData);
    }

    @Test
    public void failStartElementNotWebTable() throws XMLStreamException {
      startElement = getStartElement("p");
      dataTableXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
      Mockito.verify(mockStripElementAndContentsXMLEventHandler, Mockito.times(1)).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
    }

    @Test
    public void shouldPassStartElementIfValidTag() throws XMLStreamException{
        String elementName = "asset1";
        Map<String,String> attributes =  new HashMap<String,String>();
        attributes.put("class", "data-table");
        startElement = getStartElementWithAttributes("table", attributes);

        when(mockDataTableData.isOkToRender()).thenReturn(true);
        when(mockDataTableData.getAsset()).thenReturn(mockAsset);
        when(mockAsset.getName()).thenReturn(elementName);
        when(mockBodyProcessingContext.addAsset(Mockito.isA(Asset.class))).thenReturn(mockAsset);

        dataTableXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter).writeAsideElement(mockEventWriter, elementName, "dataTable", false);
        verify(mockDataTableXMLParser).transformFieldContentToStructuredFormat(mockDataTableData, mockBodyProcessingContext);
    }

    @Test
   public void shouldNotWriteTheAsideTag() throws XMLStreamException {
        String elementName = "asset1";
        Map<String,String> attributes =  new HashMap<String,String>();
        attributes.put("class", "data-table");
        startElement = getStartElementWithAttributes("table", attributes);

       when(mockDataTableData.isOkToRender()).thenReturn(false);
       when(mockAsset.getName()).thenReturn(elementName);

        dataTableXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

       verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "dataTable", false);
       verify(mockDataTableXMLParser, never()).transformFieldContentToStructuredFormat(mockDataTableData, mockBodyProcessingContext);
   }

   @Test
   public void shouldNotWriteTheAsideTagMissingBodyElement() throws XMLStreamException {
       String elementName = "asset1";
       Map<String,String> attributes =  new HashMap<String,String>();
       attributes.put("class", "data-table");
       startElement = getStartElementWithAttributes("table", attributes);

       when(mockDataTableData.getBody()).thenReturn(null);
       String quoteText = "some text";
       when(mockDataTableData.isOkToRender()).thenReturn(false);
       when(mockAsset.getName()).thenReturn(elementName);

        dataTableXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

       verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "dataTable", false);
       verify(mockStAXTransformingBodyProcessor, never()).process(quoteText, mockBodyProcessingContext);
       verify(mockDataTableXMLParser, never()).transformFieldContentToStructuredFormat(mockDataTableData, mockBodyProcessingContext);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testWithNullXMLParser() {
       new DataTableXMLEventHandler(null, mockAsideElementWriter, mockStripElementAndContentsXMLEventHandler);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testWithNullAsideElementWriter() {
       new DataTableXMLEventHandler(mockDataTableXMLParser, null, mockStripElementAndContentsXMLEventHandler);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testWithNullFallbackHandler() {
       new DataTableXMLEventHandler(mockDataTableXMLParser, mockAsideElementWriter, null);
   }
}
