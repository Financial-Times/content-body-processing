package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.api.ucm.model.v1.TypeBasedImage;
import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.ImageAttribute;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BlogPostImageRetainWithSpecificAttributesXMLEventHandlerTest extends BaseXMLEventHandlerTest {

    @Mock
    private BodyProcessingContext bodyProcessingContext;
    @Mock
    private BodyWriter eventWriter;
    @Mock
    private XMLEventReader eventReader;

    private StartElement startElement;

    private BlogPostImageRetainWithSpecificAttributesXMLEventHandler
            blogPostImageRetainWithSpecificAttributesXMLEventHandler;

    @Before
    public void setUp() {
        blogPostImageRetainWithSpecificAttributesXMLEventHandler = new
                BlogPostImageRetainWithSpecificAttributesXMLEventHandler();
    }

    @Test
    public void shouldAddProcessedImageAttributesAsImageToProcessingContext() throws Exception {
        Map<String, String> expectedImageAttributes = ImmutableMap
                .of(ImageAttribute.SRC.getAttributeName(), "someImage.jpg", ImageAttribute.ALT.getAttributeName(), "someAlt", ImageAttribute.IMAGE_ID.getAttributeName(), "275702");

        blogPostImageRetainWithSpecificAttributesXMLEventHandler.handleStartElementEvent
                (getStartElementWithAttributes("img", expectedImageAttributes), eventReader,
                eventWriter, bodyProcessingContext);

        verify(bodyProcessingContext).addImageWithAttributes(expectedImageAttributes, TypeBasedImage.ImageType.INLINE_EXT);
        verify(eventWriter).writeStartTag("img", expectedImageAttributes);
    }

    @Test
    public void imageIdShouldBeExtractedFromClassAttributeIfNotPresentOnImgClass() throws Exception {
        Map<String, String> expectedImageAttributes = ImmutableMap
                .of(ImageAttribute.SRC.getAttributeName(), "someImage.jpg", ImageAttribute.ALT.getAttributeName(),
                        "someAlt", ImageAttribute.IMAGE_ID.getAttributeName(), "someId");


        startElement = getStartElementWithAttributes("img", ImmutableMap.of(ImageAttribute.SRC.getAttributeName(),
                "someImage.jpg", ImageAttribute.ALT.getAttributeName(), "someAlt", "class", "wp-image-someId"));
        when(eventReader.hasNext()).thenReturn(true);

        blogPostImageRetainWithSpecificAttributesXMLEventHandler.handleStartElementEvent(startElement, eventReader, eventWriter, bodyProcessingContext);

        verify(bodyProcessingContext).addImageWithAttributes(expectedImageAttributes, TypeBasedImage.ImageType.INLINE_EXT);
        verify(eventWriter).writeStartTag("img", expectedImageAttributes);
    }

    @Test
    public void imageIdShouldBeExtractedFromMultipleClassAttributeIfNotPresentOnImgClass() throws Exception {
        Map<String, String> expectedImageAttributes = ImmutableMap
                .of(ImageAttribute.SRC.getAttributeName(), "someImage.jpg", ImageAttribute.ALT.getAttributeName(),
                        "someAlt", ImageAttribute.IMAGE_ID.getAttributeName(), "someId");


        startElement = getStartElementWithAttributes("img", ImmutableMap.of(ImageAttribute.SRC.getAttributeName(),
                "someImage.jpg", ImageAttribute.ALT.getAttributeName(), "someAlt",
                "class", "someOtherClass wp-image-someId"));
        when(eventReader.hasNext()).thenReturn(true);

        blogPostImageRetainWithSpecificAttributesXMLEventHandler.handleStartElementEvent(startElement, eventReader, eventWriter, bodyProcessingContext);

        verify(bodyProcessingContext).addImageWithAttributes(expectedImageAttributes, TypeBasedImage.ImageType.INLINE_EXT);
        verify(eventWriter).writeStartTag("img", expectedImageAttributes);
    }

    @Test
    public void imageIdShouldNotBeAddedToTheContextIfNotPresentAtAll() throws Exception {
        Map<String, String> expectedImageAttributes = ImmutableMap
                .of(ImageAttribute.SRC.getAttributeName(), "someImage.jpg", ImageAttribute.ALT.getAttributeName(),
                        "someAlt");


        startElement = getStartElementWithAttributes("img", ImmutableMap.of(ImageAttribute.SRC.getAttributeName(),
                "someImage.jpg", ImageAttribute.ALT.getAttributeName(), "someAlt"));
        when(eventReader.hasNext()).thenReturn(true);

        blogPostImageRetainWithSpecificAttributesXMLEventHandler.handleStartElementEvent(startElement, eventReader, eventWriter, bodyProcessingContext);

        verify(bodyProcessingContext).addImageWithAttributes(expectedImageAttributes, TypeBasedImage.ImageType.INLINE_EXT);
        verify(eventWriter).writeStartTag("img", expectedImageAttributes);
    }

    @Test
    public void imageIdShouldNotBeExtractedFromClassAttributeIfImageIdAttributeIsPresent() throws Exception {
        Map<String, String> expectedImageAttributes = ImmutableMap
                .of(ImageAttribute.SRC.getAttributeName(), "someImage.jpg", ImageAttribute.ALT.getAttributeName(),
                        "someAlt", ImageAttribute.IMAGE_ID.getAttributeName(), "someId");


        startElement = getStartElementWithAttributes("img", ImmutableMap.of(ImageAttribute.SRC.getAttributeName(),
                "someImage.jpg", ImageAttribute.ALT.getAttributeName(), "someAlt",
                ImageAttribute.IMAGE_ID.getAttributeName(), "someId", "class", "wp-image-someOtherId"));
        when(eventReader.hasNext()).thenReturn(true);

        blogPostImageRetainWithSpecificAttributesXMLEventHandler.handleStartElementEvent(startElement, eventReader, eventWriter, bodyProcessingContext);

        verify(bodyProcessingContext).addImageWithAttributes(expectedImageAttributes, TypeBasedImage.ImageType.INLINE_EXT);
        verify(eventWriter).writeStartTag("img", expectedImageAttributes);
    }

    @Test
    public void imageIdShouldNotBeExtractedFromClassAttributeIfPatternIsNotMatching() throws Exception {
        Map<String, String> expectedImageAttributes = ImmutableMap
                .of(ImageAttribute.SRC.getAttributeName(), "someImage.jpg", ImageAttribute.ALT.getAttributeName(),
                        "someAlt");


        startElement = getStartElementWithAttributes("img", ImmutableMap.of(ImageAttribute.SRC.getAttributeName(),
                "someImage.jpg", ImageAttribute.ALT.getAttributeName(), "someAlt", "class", "someClassAttribute"));
        when(eventReader.hasNext()).thenReturn(true);

        blogPostImageRetainWithSpecificAttributesXMLEventHandler.handleStartElementEvent(startElement, eventReader, eventWriter, bodyProcessingContext);

        verify(bodyProcessingContext).addImageWithAttributes(expectedImageAttributes, TypeBasedImage.ImageType.INLINE_EXT);
        verify(eventWriter).writeStartTag("img", expectedImageAttributes);
    }
}
