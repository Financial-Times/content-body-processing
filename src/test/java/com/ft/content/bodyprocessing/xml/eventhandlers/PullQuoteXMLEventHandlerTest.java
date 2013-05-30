package com.ft.content.bodyprocessing.xml.eventhandlers;

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

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.ft.content.bodyprocessing.xml.StAXTransformingBodyProcessor;
import com.ft.api.ucm.model.v1.Asset;

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
    public void setup() throws XMLStreamException {
        pullQuoteXMLEventHandler = new PullQuoteXMLEventHandler(mockPullQuoteXMLParser, mockAsideElementWriter);
        when(mockPullQuoteXMLParser.parseElementData(Mockito.isA(StartElement.class), Mockito.eq(mockXmlEventReader))).thenReturn(mockPullQuoteData);
    }

    @Test(expected=BodyProcessingException.class)
    public void failStartElementNotPullQuote() throws XMLStreamException {
        startElement = getStartElement("p");
        pullQuoteXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
    }
    
   @Test
    public void shouldPassStartElementIfValidTag() throws XMLStreamException{
        String elementName = "asset1";
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("web-pull-quote", attributes);
        
        when(mockPullQuoteData.isAllRequiredDataPresent()).thenReturn(true);
        when(mockPullQuoteData.getAsset()).thenReturn(mockAsset);
        when(mockAsset.getName()).thenReturn(elementName);
        when(mockBodyProcessingContext.addAsset(Mockito.isA(Asset.class))).thenReturn(mockAsset);
        
        pullQuoteXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter).writeAsideElement(mockEventWriter, elementName, "pullQuote");
        verify(mockPullQuoteXMLParser).transformFieldContentToStructuredFormat(mockPullQuoteData, mockBodyProcessingContext);
    }
   
   @Test
   public void shouldNotWriteTheAsideTag() throws XMLStreamException {
       String elementName = "someElementName";
       Map<String,String> attributes =  new HashMap<String,String>();
       startElement = getStartElementWithAttributes("web-pull-quote", attributes);

       when(mockPullQuoteData.isAllRequiredDataPresent()).thenReturn(false);
       when(mockAsset.getName()).thenReturn(elementName);
       
       pullQuoteXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

       verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "pullquote");
       verify(mockPullQuoteXMLParser, never()).transformFieldContentToStructuredFormat(mockPullQuoteData, mockBodyProcessingContext);
   }
   
   @Test
   public void shouldNotWriteTheAsideTagMissingSourceElement() throws XMLStreamException {
       String elementName = "someElementName";
       Map<String,String> attributes =  new HashMap<String,String>();
       startElement = getStartElementWithAttributes("web-pull-quote", attributes);
       
       when(mockPullQuoteData.getQuoteSource()).thenReturn(null);
       String quoteText = "some text";
       when(mockPullQuoteData.getQuoteText()).thenReturn(quoteText);
       when(mockPullQuoteData.isAllRequiredDataPresent()).thenReturn(false);
       when(mockAsset.getName()).thenReturn(elementName);
       
       pullQuoteXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

       verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "pullquote");
       verify(mockStAXTransformingBodyProcessor, never()).process(quoteText, mockBodyProcessingContext);
       verify(mockPullQuoteXMLParser, never()).transformFieldContentToStructuredFormat(mockPullQuoteData, mockBodyProcessingContext);
   }
   
   @Test
   public void shouldNotWriteAsideTagNoValidDataAfterTransformParsedData() throws XMLStreamException {
       String elementName = "someElementName";
       Map<String,String> attributes =  new HashMap<String,String>();
       startElement = getStartElementWithAttributes("web-pull-quote", attributes);
       
       String sourceText = "some source text";
       when(mockPullQuoteData.getQuoteSource()).thenReturn(sourceText);
       String quoteText = "some text";
       when(mockPullQuoteData.getQuoteText()).thenReturn(quoteText);
       when(mockPullQuoteData.isAllRequiredDataPresent()).thenReturn(true).thenReturn(false);
       
       when(mockAsset.getName()).thenReturn(elementName);
       
       pullQuoteXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

       verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "pullquote");
       verify(mockStAXTransformingBodyProcessor, never()).process(quoteText, mockBodyProcessingContext);
       verify(mockPullQuoteXMLParser).transformFieldContentToStructuredFormat(mockPullQuoteData, mockBodyProcessingContext);
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
