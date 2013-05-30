package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.writer.BodyWriter;
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
public class BlogPostAssetXMLEventHandlerTest extends BaseXMLEventHandlerTest {
    @Mock
    private BodyWriter mockEventWriter;
    @Mock private XMLEventReader2 mockXmlEventReader;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private Asset mockAsset;
    @Mock private XmlParser<VideoData> xmlParser;
    @Mock private AsideElementWriter mockAsideElementWriter;
    @Mock private VideoData mockVideoData;
    @Mock private XMLEventHandler mockXmlEventHandler;

    private StartElement startElement;
    private BlogPostAssetXMLEventHandler<VideoData> blogPostAssetXMLEventHandlerUnderTest;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() throws XMLStreamException {
        blogPostAssetXMLEventHandlerUnderTest = new BlogPostAssetXMLEventHandler<VideoData>(xmlParser, "video", mockAsideElementWriter, mockXmlEventHandler);
        when(xmlParser.parseElementData(any(StartElement.class), eq(mockXmlEventReader)))
                .thenReturn(mockVideoData);
    }


    @Test
    public void cannotCreateWithNullBlogPostVideoXMLParser() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("assetXMLParser cannot be null");
        new BlogPostAssetXMLEventHandler<VideoData>(null, "video", mockAsideElementWriter, mockXmlEventHandler);
    }

    @Test
    public void cannotCreateWithNullAsideElementWriter() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("AsideElementWriter cannot be null");
        new BlogPostAssetXMLEventHandler<VideoData>(xmlParser, "tweet", null, mockXmlEventHandler);
    }

    @Test
    public void cannotCreateWithNullFallback() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("fallbackHandler cannot be null");
        new BlogPostAssetXMLEventHandler<VideoData>(xmlParser, "video", mockAsideElementWriter, null);
    }

    @Test
    public void cannotCreateWithNullAssetType() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("assetType cannot be null");
        new BlogPostAssetXMLEventHandler<VideoData>(xmlParser, null, mockAsideElementWriter, mockXmlEventHandler);
    }

    @Test
    public void typeIsAsPassedInConstructor() {
        BlogPostAssetXMLEventHandler handler = new BlogPostAssetXMLEventHandler<VideoData>(xmlParser, "some type", mockAsideElementWriter, mockXmlEventHandler);
        assertThat(handler.getType(), is("some type"));
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

        blogPostAssetXMLEventHandlerUnderTest.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter,
                mockBodyProcessingContext);

        verify(mockAsideElementWriter).writeAsideElement(mockEventWriter, elementName, "video");
        verify(xmlParser, never()).transformFieldContentToStructuredFormat(mockVideoData, mockBodyProcessingContext);
    }
}
