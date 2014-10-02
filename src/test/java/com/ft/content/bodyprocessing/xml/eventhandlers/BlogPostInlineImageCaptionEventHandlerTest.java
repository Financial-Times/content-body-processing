package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.ImageAttribute;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.stream.XMLStreamException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BlogPostInlineImageCaptionEventHandlerTest extends BlogPostInlineImageDataEventHandlerTest {

    private BlogPostInlineImageCaptionEventHandler blogPostInlineImageCaptionEventHandler;

    @Before
    public void setUp() throws Exception {
        setBlogPostInlineImageDataEventHandler(new BlogPostInlineImageCaptionEventHandler(mockFallbackXmlEventHandler, mockTransformingBodyProcessor));
    }

    @Test
    public void testCaptionShouldBeAddedToTheContext() throws Exception {
        startElement = getStartElementWithAttributes(getImageDataElement(), ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("caption");
        endElement = getEndElement(getImageDataElement());
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenReturn(characters).thenReturn(endElement);
        when(mockTransformingBodyProcessor.process(characters.getData(), mockBodyProcessingContext)).thenReturn(characters.getData());
        getBlogPostInlineImageDataEventHandler().handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockBodyProcessingContext).addAttributesToExistingImageWithId(eq("someId"), eq(ImmutableMap.of(ImageAttribute.CAPTION.getAttributeName(), "caption")));
    }

    @Test
    public void testCaptionShouldNotBeAddedToTheContextWhenXMLStreamExceptionThrown() throws Exception {
        startElement = getStartElementWithAttributes(getImageDataElement(), ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("caption");
        endElement = getEndElement(getImageDataElement());
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenThrow(new XMLStreamException());

        getBlogPostInlineImageDataEventHandler().handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verifyZeroInteractions(mockBodyProcessingContext);
    }

    @Test
    public void testEmptyCaptionShouldNotBeAddedToTheContext() throws Exception {
        startElement = getStartElementWithAttributes(getImageDataElement(), ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("");
        endElement = getEndElement(getImageDataElement());
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenReturn(characters).thenReturn(endElement);

        getBlogPostInlineImageDataEventHandler().handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verifyZeroInteractions(mockBodyProcessingContext);
    }

    @Test
    public void testCaptionShouldBeAddedToTheContextWithSourceContentWhenSourceHasNoImageIdAttribute() throws Exception {
        startElement = getStartElementWithAttributes(getImageDataElement(), ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("caption<span> source</span>");
        endElement = getEndElement(getImageDataElement());
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenReturn(characters).thenReturn(endElement);
        when(mockTransformingBodyProcessor.process(any(String.class), eq(mockBodyProcessingContext))).thenReturn("caption source");
        getBlogPostInlineImageDataEventHandler().handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockBodyProcessingContext).addAttributesToExistingImageWithId(eq("someId"), eq(ImmutableMap.of(ImageAttribute.CAPTION.getAttributeName(), "caption source")));
    }

    @Test
    public void testCaptionShouldBeAddedToTheContextWithoutSourceContentWhenSourceHasImageIdAttribute() throws Exception {
        startElement = getStartElementWithAttributes(getImageDataElement(), ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("caption<span " + ImageAttribute.IMAGE_ID.getAttributeName() + "='someId'>source</span>");
        endElement = getEndElement(getImageDataElement());
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenReturn(characters).thenReturn(endElement);
        when(mockTransformingBodyProcessor.process(any(String.class), eq(mockBodyProcessingContext))).thenReturn("caption");
        getBlogPostInlineImageDataEventHandler().handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockBodyProcessingContext).addAttributesToExistingImageWithId(eq("someId"), eq(ImmutableMap.of(ImageAttribute.CAPTION.getAttributeName(), "caption")));
    }

    public void setBlogPostInlineImageDataEventHandler(BlogPostInlineImageCaptionEventHandler blogPostInlineImageCaptionEventHandler) {
        this.blogPostInlineImageCaptionEventHandler = blogPostInlineImageCaptionEventHandler;
    }

    @Override
    public BlogPostInlineImageCaptionEventHandler getBlogPostInlineImageDataEventHandler() {
        return blogPostInlineImageCaptionEventHandler;
    }

    protected String getImageDataElement() {
        return "p";
    }
}
