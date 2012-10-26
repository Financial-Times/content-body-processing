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
import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.StAXTransformingBodyProcessor;
import com.ft.unifiedContentModel.model.Asset;

@RunWith(MockitoJUnitRunner.class)
public class InteractiveGraphicXMLEventHandlerTest extends BaseXMLEventHandlerTest {

    @Mock private BodyWriter mockEventWriter;
    @Mock private XMLEventReader2 mockXmlEventReader;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private Asset mockAsset;
    @Mock private InteractiveGraphicXMLParser mockInteractiveGraphicXMLParser;
    @Mock private AsideElementWriter mockAsideElementWriter;
    @Mock private InteractiveGraphicData mockInteractiveGraphicData;
    @Mock private StAXTransformingBodyProcessor mockStAXTransformingBodyProcessor;
    
    private StartElement startElement;
    private InteractiveGraphicXMLEventHandler interactiveGraphicXMLEventHandler;
    private String elementName = "someElementName";
    
    @Before
    public void setup() throws XMLStreamException {
        interactiveGraphicXMLEventHandler = new InteractiveGraphicXMLEventHandler(mockInteractiveGraphicXMLParser, mockAsideElementWriter);
        when(mockInteractiveGraphicXMLParser.parseElementData(Mockito.isA(StartElement.class), Mockito.eq(mockXmlEventReader))).thenReturn(mockInteractiveGraphicData);
        when(mockAsset.getName()).thenReturn(elementName);
    }
    
    @Test
    public void shouldPassStartElementIfValidTag() throws XMLStreamException{
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("plainHtml", attributes);
        
        when(mockInteractiveGraphicData.isAllRequiredDataPresent()).thenReturn(true);
        when(mockInteractiveGraphicData.getAsset()).thenReturn(mockAsset);
        when(mockBodyProcessingContext.addAsset(Mockito.isA(Asset.class))).thenReturn(mockAsset);
        
        interactiveGraphicXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter).writeAsideElement(mockEventWriter, elementName, "interactiveGraphic");
        verify(mockInteractiveGraphicXMLParser).transformFieldContentToStructuredFormat(mockInteractiveGraphicData, mockBodyProcessingContext);
    }
    
    @Test
    public void shouldNotWriteTheAsideTag() throws XMLStreamException {
        
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("plainHtml", attributes);
        
        when(mockInteractiveGraphicData.isAllRequiredDataPresent()).thenReturn(false);
        
        interactiveGraphicXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "interactiveGraphic");
        verify(mockInteractiveGraphicXMLParser, never()).transformFieldContentToStructuredFormat(mockInteractiveGraphicData, mockBodyProcessingContext);
    }
    
    @Test
    public void shouldNotWriteTheAsideTagMissingIdElement() throws XMLStreamException {
        String elementName = "someElementName";
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("plainHtml", attributes);
        
        when(mockInteractiveGraphicData.getId()).thenReturn(null);
        String srcText = "some text";
        when(mockInteractiveGraphicData.getSrc()).thenReturn(srcText);
        when(mockInteractiveGraphicData.isAllRequiredDataPresent()).thenReturn(false);
        
        interactiveGraphicXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "interactiveGraphic");
        verify(mockStAXTransformingBodyProcessor, never()).process(srcText, mockBodyProcessingContext);
        verify(mockInteractiveGraphicXMLParser, never()).transformFieldContentToStructuredFormat(mockInteractiveGraphicData, mockBodyProcessingContext);
    }
    
    @Test
    public void shouldNotWriteTheAsideTagMissingIdElementAfterTransformation() throws XMLStreamException {
        String elementName = "someElementName";
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("plainHtml", attributes);
        
        String idText = "idtext";
        when(mockInteractiveGraphicData.getId()).thenReturn(idText);
        String srcText = "some text";
        when(mockInteractiveGraphicData.getSrc()).thenReturn(srcText);
        when(mockInteractiveGraphicData.isAllRequiredDataPresent()).thenReturn(true).thenReturn(false);
        
        interactiveGraphicXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "interactiveGraphic");
        verify(mockStAXTransformingBodyProcessor, never()).process(srcText, mockBodyProcessingContext);
        verify(mockInteractiveGraphicXMLParser).transformFieldContentToStructuredFormat(mockInteractiveGraphicData, mockBodyProcessingContext);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testWithNullPullQuoteXMLParser() {
        new InteractiveGraphicXMLEventHandler(null, mockAsideElementWriter);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testWithNullAsideElementWriter() {
        new InteractiveGraphicXMLEventHandler(mockInteractiveGraphicXMLParser, null);
    }
    
    @Test(expected = BodyProcessingException.class)
    public void testWithMismatchRootElement() throws XMLStreamException {
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("someOtherElement", attributes);
        
        interactiveGraphicXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
    }
}
