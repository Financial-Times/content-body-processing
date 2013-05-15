package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.api.ucm.model.v1.Asset;
import com.google.common.collect.ImmutableMap;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BlogPostVideoXMLEventHandlerTest extends BaseXMLEventHandlerTest {
    @Mock
    private BodyWriter mockEventWriter;
    @Mock private XMLEventReader2 mockXmlEventReader;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private Asset mockAsset;
    @Mock private BlogPostVideoXMLParser mockVideoXMLParser;
    @Mock private AsideElementWriter mockAsideElementWriter;
    @Mock private VideoData mockVideoData;
    @Mock private XMLEventHandler mockXmlEventHandler;

    private StartElement startElement;
    private BlogPostVideoXMLEventHandler blogPostVideoXMLEventHandlerUnderTest;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() throws XMLStreamException {
        blogPostVideoXMLEventHandlerUnderTest = new BlogPostVideoXMLEventHandler(mockVideoXMLParser, mockAsideElementWriter, mockXmlEventHandler);
        when(mockVideoXMLParser.parseElementData(any(StartElement.class), eq(mockXmlEventReader)))
                .thenReturn(mockVideoData);
    }


    @Test
    public void cannotCreateWithNullBlogPostVideoXMLParser() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("blogPostVideoXMLParser cannot be null");
        new BlogPostVideoXMLEventHandler(null, mockAsideElementWriter, mockXmlEventHandler);
    }

    @Test
    public void cannotCreateWithNullAsideElementWriter() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("AsideElementWriter cannot be null");
        new BlogPostVideoXMLEventHandler(mockVideoXMLParser, null, mockXmlEventHandler);
    }

    @Test
    public void cannotCreateWithNullFallback() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("fallbackHandler cannot be null");
        new BlogPostVideoXMLEventHandler(mockVideoXMLParser, mockAsideElementWriter, null);
    }

    @Test
    public void shouldPassStartElementIfValidTag() throws XMLStreamException {
        String elementName = "asset1";
        Map<String,String> attributes =  ImmutableMap.of("data-asset-type","video","data-asset-source","Brightcove","data-asset-ref","234234878");
        startElement = getStartElementWithAttributes("div", attributes);

        when(mockVideoData.isAllRequiredDataPresent()).thenReturn(true);
        when(mockVideoData.getAsset()).thenReturn(mockAsset);
        when(mockAsset.getName()).thenReturn(elementName);
        when(mockBodyProcessingContext.addAsset(Mockito.isA(Asset.class))).thenReturn(mockAsset);

        blogPostVideoXMLEventHandlerUnderTest.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter,
                mockBodyProcessingContext);

        verify(mockAsideElementWriter).writeAsideElement(mockEventWriter, elementName, "video");
        verify(mockVideoXMLParser, never()).transformFieldContentToStructuredFormat(mockVideoData, mockBodyProcessingContext);
    }
}
