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
import com.ft.unifiedContentModel.model.Asset;

@RunWith(MockitoJUnitRunner.class)
public class PromoBoxXMLEventHandlerTest extends BaseXMLEventHandlerTest {

    @Mock private BodyWriter mockEventWriter;
    @Mock private XMLEventReader2 mockXmlEventReader;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private Asset mockAsset;
    @Mock private PromoBoxXMLParser mockPromoBoxXMLParser;
    @Mock private AsideElementWriter mockAsideElementWriter;
    @Mock private PromoBoxData mockPromoBox;
        
    private StartElement startElement;
    private PromoBoxXMLEventHandler promoBoxXMLEventHandler;
    private String elementName = "someElementName";
    
    @Before
    public void setup() throws XMLStreamException {
        promoBoxXMLEventHandler = new PromoBoxXMLEventHandler(mockPromoBoxXMLParser, mockAsideElementWriter);
        when(mockPromoBoxXMLParser.parseElementData(Mockito.isA(StartElement.class), Mockito.eq(mockXmlEventReader))).thenReturn(mockPromoBox);
        when(mockAsset.getName()).thenReturn(elementName);
    }
    
    @Test
    public void shouldPassStartElementIfValidTag() throws XMLStreamException{
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("promo-box", attributes);
        
        when(mockPromoBox.isOkToRender()).thenReturn(true);
        when(mockPromoBox.getAsset()).thenReturn(mockAsset);
        when(mockBodyProcessingContext.addAsset(Mockito.isA(Asset.class))).thenReturn(mockAsset);
        
        promoBoxXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter).writeAsideElement(mockEventWriter, elementName, "promoBox");
        verify(mockPromoBoxXMLParser).transformFieldContentToStructuredFormat(mockPromoBox, mockBodyProcessingContext);
    }
    
    @Test
    public void shouldNotWriteTheAsideTag() throws XMLStreamException {
        
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("promo-box", attributes);
        
        when(mockPromoBox.isOkToRender()).thenReturn(false);
        
        promoBoxXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "promoBox");
        verify(mockPromoBoxXMLParser, never()).transformFieldContentToStructuredFormat(mockPromoBox, mockBodyProcessingContext);
    }
    
    @Test
    public void shouldNotWriteTheAsideTagMissingIdElement() throws XMLStreamException {
        String elementName = "someElementName";
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("promo-box", attributes);
        
        when(mockPromoBox.isOkToRender()).thenReturn(false);
        
        promoBoxXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "promoBox");
        verify(mockPromoBoxXMLParser, never()).transformFieldContentToStructuredFormat(mockPromoBox, mockBodyProcessingContext);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testWithNullPullQuoteXMLParser() {
        new InteractiveGraphicXMLEventHandler(null, mockAsideElementWriter);
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testWithNullAsideElementWriter() {
        new PromoBoxXMLEventHandler(mockPromoBoxXMLParser, null);
    }
}
