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
    
    @Before
    public void setup() {
        interactiveGraphicXMLEventHandler = new InteractiveGraphicXMLEventHandler(mockInteractiveGraphicXMLParser, mockAsideElementWriter);
    }
    
    @Test
    public void shouldPassStartElementIfValidTag() throws XMLStreamException{
        String elementName = "asset1";
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("plainHtml", attributes);
        
        when(mockInteractiveGraphicXMLParser.parseElementData(mockXmlEventReader)).thenReturn(mockInteractiveGraphicData);
        when(mockStAXTransformingBodyProcessor.process(Mockito.anyString(), Mockito.eq(mockBodyProcessingContext))).thenReturn("some content");
        when(mockInteractiveGraphicData.isOkToRender()).thenReturn(true);
        when(mockInteractiveGraphicData.getAsset()).thenReturn(mockAsset);
        when(mockAsset.getName()).thenReturn(elementName);
        when(mockBodyProcessingContext.addAsset(Mockito.isA(Asset.class))).thenReturn(mockAsset);
        
        interactiveGraphicXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter).writeAsideElement(mockEventWriter, elementName, "interactiveGraphic", false);
        verify(mockInteractiveGraphicXMLParser).transformFieldContentToStructuredFormat(mockInteractiveGraphicData, mockBodyProcessingContext);
    }
    
    @Test
    public void shouldNotWriteTheAsideTag() throws XMLStreamException {
        String elementName = "someElementName";
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("plainHtml", attributes);
        
        when(mockInteractiveGraphicXMLParser.parseElementData(mockXmlEventReader)).thenReturn(mockInteractiveGraphicData);
        when(mockStAXTransformingBodyProcessor.process(Mockito.anyString(), Mockito.eq(mockBodyProcessingContext))).thenReturn("some content");
        when(mockInteractiveGraphicData.isOkToRender()).thenReturn(false);
        when(mockAsset.getName()).thenReturn(elementName);
        
        interactiveGraphicXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "interactiveGraphic", true);
        verify(mockInteractiveGraphicXMLParser, never()).transformFieldContentToStructuredFormat(mockInteractiveGraphicData, mockBodyProcessingContext);
    }
    
    @Test
    public void shouldNotWriteTheAsideTagMissingIdElement() throws XMLStreamException {
        String elementName = "someElementName";
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("plainHtml", attributes);
        
        when(mockInteractiveGraphicXMLParser.parseElementData(mockXmlEventReader)).thenReturn(mockInteractiveGraphicData);
        when(mockStAXTransformingBodyProcessor.process(Mockito.anyString(), Mockito.eq(mockBodyProcessingContext))).thenReturn("some content");
        
        when(mockInteractiveGraphicData.getId()).thenReturn(null);
        String srcText = "some text";
        when(mockInteractiveGraphicData.getSrc()).thenReturn(srcText);
        when(mockInteractiveGraphicData.isOkToRender()).thenReturn(false);
        when(mockAsset.getName()).thenReturn(elementName);
        
        interactiveGraphicXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "interactiveGraphic", true);
        verify(mockStAXTransformingBodyProcessor, never()).process(srcText, mockBodyProcessingContext);
        verify(mockInteractiveGraphicXMLParser, never()).transformFieldContentToStructuredFormat(mockInteractiveGraphicData, mockBodyProcessingContext);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testWithNullPullQuoteXMLParser() {
        new InteractiveGraphicXMLEventHandler(null, mockAsideElementWriter);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testWithNullAsideElementWriter() {
        new InteractiveGraphicXMLEventHandler(mockInteractiveGraphicXMLParser, null);
    }
}
