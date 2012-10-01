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
public class PullQuoteXMLEventHandlerTest extends BaseXMLEventHandlerTest {

    @Mock private BodyWriter mockEventWriter;
    @Mock private XMLEventReader2 mockXmlEventReader;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private Asset mockAsset;
    @Mock private PullQuoteXMLParser mockPullQuoteXMLParser;
    @Mock private AsideElementWriter mockAsideElementWriter;
    @Mock private PullQuoteData mockPullQuoteData;
    @Mock private StAXTransformingBodyProcessor mockStAXTransformingBodyProcessor;
    
    private StartElement startElement;
    private PullQuoteXMLEventHandler pullQuoteXMLEventHandler;
    
    @Before
    public void setup() {
        pullQuoteXMLEventHandler = new PullQuoteXMLEventHandler(mockPullQuoteXMLParser, mockAsideElementWriter);
    }

    @Test(expected=XMLStreamException.class)
    public void failStartElementNotPullQuote() throws XMLStreamException {
        startElement = getStartElement("p");
        pullQuoteXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
    }
    
   @Test
    public void shouldPassStartElementIfValidTag() throws XMLStreamException{
        String elementName = "asset1";
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("web-pull-quote", attributes);
        
        when(mockPullQuoteXMLParser.parseElementData(mockXmlEventReader)).thenReturn(mockPullQuoteData);
        when(mockStAXTransformingBodyProcessor.process(Mockito.anyString(), Mockito.eq(mockBodyProcessingContext))).thenReturn("some content");
        when(mockPullQuoteData.isOkToRender()).thenReturn(true);
        when(mockPullQuoteData.getAsset()).thenReturn(mockAsset);
        when(mockAsset.getName()).thenReturn(elementName);
        when(mockBodyProcessingContext.addAsset(Mockito.isA(Asset.class))).thenReturn(mockAsset);
        
        pullQuoteXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter).writeAsideElement(mockEventWriter, elementName, "pullQuote", true);
        verify(mockPullQuoteXMLParser).transformFieldContentToStructuredFormat(mockPullQuoteData, mockBodyProcessingContext);
    }
   
   @Test
   public void shouldNotWriteTheAsideTag() throws XMLStreamException {
       String elementName = "someElementName";
       Map<String,String> attributes =  new HashMap<String,String>();
       startElement = getStartElementWithAttributes("web-pull-quote", attributes);
       
       when(mockPullQuoteXMLParser.parseElementData(mockXmlEventReader)).thenReturn(mockPullQuoteData);
       when(mockStAXTransformingBodyProcessor.process(Mockito.anyString(), Mockito.eq(mockBodyProcessingContext))).thenReturn("some content");
       when(mockPullQuoteData.isOkToRender()).thenReturn(false);
       when(mockAsset.getName()).thenReturn(elementName);
       
       pullQuoteXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

       verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "pullquote", true);
       verify(mockPullQuoteXMLParser, never()).transformFieldContentToStructuredFormat(mockPullQuoteData, mockBodyProcessingContext);
   }
   
   @Test
   public void shouldNotWriteTheAsideTagMissingSourceElement() throws XMLStreamException {
       String elementName = "someElementName";
       Map<String,String> attributes =  new HashMap<String,String>();
       startElement = getStartElementWithAttributes("web-pull-quote", attributes);
       
       when(mockPullQuoteXMLParser.parseElementData(mockXmlEventReader)).thenReturn(mockPullQuoteData);
       when(mockStAXTransformingBodyProcessor.process(Mockito.anyString(), Mockito.eq(mockBodyProcessingContext))).thenReturn("some content");
       
       when(mockPullQuoteData.getQuoteSource()).thenReturn(null);
       String quoteText = "some text";
       when(mockPullQuoteData.getQuoteText()).thenReturn(quoteText);
       when(mockPullQuoteData.isOkToRender()).thenReturn(false);
       when(mockAsset.getName()).thenReturn(elementName);
       
       pullQuoteXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

       verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "pullquote", true);
       verify(mockStAXTransformingBodyProcessor, never()).process(quoteText, mockBodyProcessingContext);
       verify(mockPullQuoteXMLParser, never()).transformFieldContentToStructuredFormat(mockPullQuoteData, mockBodyProcessingContext);
   }
   
   @Test(expected = IllegalArgumentException.class) 
   public void testWithNullPullQuoteXMLParser() {
       new PullQuoteXMLEventHandler(null, mockAsideElementWriter);
   }
   
   @Test(expected = IllegalArgumentException.class) 
   public void testWithNullAsideElementWriter() {
       new PullQuoteXMLEventHandler(mockPullQuoteXMLParser, null);
   }
}
