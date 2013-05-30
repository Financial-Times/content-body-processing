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
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.ft.api.ucm.model.v1.Asset;

@RunWith(MockitoJUnitRunner.class)
public class VideoXMLEventHandlerTest extends BaseXMLEventHandlerTest {

    @Mock private BodyWriter mockEventWriter;
    @Mock private XMLEventReader2 mockXmlEventReader;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private Asset mockAsset;
    @Mock private VideoXMLParser mockVideoXMLParser;
    @Mock private AsideElementWriter mockAsideElementWriter;
    @Mock private VideoData mockVideoData;
    @Mock private StripElementAndContentsXMLEventHandler mockStripElementAndContentsXMLEventHandler;

    private StartElement startElement;
    private VideoXMLEventHandler videoAssetXMLEventHandler;

    @Before
    public void setup() throws XMLStreamException {
        videoAssetXMLEventHandler = new VideoXMLEventHandler(mockVideoXMLParser, mockAsideElementWriter, mockStripElementAndContentsXMLEventHandler);
        when(mockVideoXMLParser.parseElementData(Mockito.any(StartElement.class), Mockito.eq(mockXmlEventReader)))
                .thenReturn(mockVideoData);
    }

    @SuppressWarnings("serial")
    @Test
    public void shouldPassStartElementIfValidTag() throws XMLStreamException {
        String elementName = "asset1";

        Map<String, String> attributes = new HashMap<String, String>() {{put("videoID", "123456");}};
        startElement = getStartElementWithAttributes("videoPlayer", attributes);

        when(mockVideoData.isAllRequiredDataPresent()).thenReturn(true);
        when(mockVideoData.getAsset()).thenReturn(mockAsset);
        when(mockAsset.getName()).thenReturn(elementName);
        when(mockBodyProcessingContext.addAsset(Mockito.isA(Asset.class))).thenReturn(mockAsset);

        videoAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter,
                mockBodyProcessingContext);

        verify(mockAsideElementWriter).writeAsideElement(mockEventWriter, elementName, "video");
        verify(mockVideoXMLParser, never()).transformFieldContentToStructuredFormat(mockVideoData, mockBodyProcessingContext);
    }

     @Test
     public void shouldNotCorrectTheAsideTag() throws XMLStreamException {
        String elementName = "someElementName";
        Map<String, String> attributes = new HashMap<String, String>();
        startElement = getStartElementWithAttributes("web-background-news", attributes);

        when(mockVideoData.isAllRequiredDataPresent()).thenReturn(false);
        when(mockAsset.getName()).thenReturn(elementName);

        videoAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter,
                mockBodyProcessingContext);

        verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "video");
        verify(mockVideoXMLParser, never()).transformFieldContentToStructuredFormat(mockVideoData, mockBodyProcessingContext);
        verify(mockStripElementAndContentsXMLEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
     }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullBackgroundNewsXMLParser() {
        new VideoXMLEventHandler(null, mockAsideElementWriter, mockStripElementAndContentsXMLEventHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullAsideElementWriter() {
        new VideoXMLEventHandler(mockVideoXMLParser, null, mockStripElementAndContentsXMLEventHandler);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithNullFallback() {
        new VideoXMLEventHandler(mockVideoXMLParser, mockAsideElementWriter, null);
    }
}
