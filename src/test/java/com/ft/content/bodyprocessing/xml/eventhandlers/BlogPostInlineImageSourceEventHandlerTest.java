package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.ImageAttribute;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.stream.XMLStreamException;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BlogPostInlineImageSourceEventHandlerTest extends BlogPostInlineImageDataEventHandlerTest {

    private BlogPostInlineImageSourceEventHandler blogPostInlineImageSourceEventHandler;

    @Before
    public void setUp() throws Exception {
        setBlogPostInlineImageDataEventHandler(new BlogPostInlineImageSourceEventHandler(mockFallbackXmlEventHandler, mockTransformingBodyProcessor));
    }

    @Test
    public void testSourceShouldBeAddedToTheContext() throws Exception {
        startElement = getStartElementWithAttributes(getImageDataElement(), ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("source");
        endElement = getEndElement(getImageDataElement());
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenReturn(characters).thenReturn(endElement);
        when(mockTransformingBodyProcessor.process(characters.getData(), mockBodyProcessingContext)).thenReturn(characters.getData());
        getBlogPostInlineImageDataEventHandler().handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockBodyProcessingContext).addAttributesToExistingImageWithId(eq("someId"), eq(ImmutableMap.of(ImageAttribute.SOURCE.getAttributeName(), "source")));
    }


    @Test
    public void testSourceShouldBeAddedToTheContextWithInnerSpanTagElementContent() throws Exception {
        startElement = getStartElementWithAttributes(getImageDataElement(), ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("source<span> span</span>");
        endElement = getEndElement(getImageDataElement());
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenReturn(characters).thenReturn(endElement);
        when(mockTransformingBodyProcessor.process(any(String.class), eq(mockBodyProcessingContext))).thenReturn("source span");
        getBlogPostInlineImageDataEventHandler().handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockBodyProcessingContext).addAttributesToExistingImageWithId(eq("someId"), eq(ImmutableMap.of(ImageAttribute.SOURCE.getAttributeName(), "source span")));
    }

    @Test
    public void testSourceShouldNotBeAddedToTheContextWhenXMLStreamExceptionThrown() throws Exception {
        startElement = getStartElementWithAttributes(getImageDataElement(), ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("source");
        endElement = getEndElement(getImageDataElement());
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenThrow(new XMLStreamException());

        getBlogPostInlineImageDataEventHandler().handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verifyZeroInteractions(mockBodyProcessingContext);
    }

    @Test
    public void testEmptySourceShouldNotBeAddedToTheContext() throws Exception {
        startElement = getStartElementWithAttributes(getImageDataElement(), ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("");
        endElement = getEndElement(getImageDataElement());
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenReturn(characters).thenReturn(endElement);

        getBlogPostInlineImageDataEventHandler().handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verifyZeroInteractions(mockBodyProcessingContext);
    }

    public void setBlogPostInlineImageDataEventHandler(BlogPostInlineImageSourceEventHandler blogPostInlineImageCaptionEventHandler) {
        this.blogPostInlineImageSourceEventHandler = blogPostInlineImageCaptionEventHandler;
    }

    @Override
    public BlogPostInlineImageSourceEventHandler getBlogPostInlineImageDataEventHandler() {
        return blogPostInlineImageSourceEventHandler;
    }

    protected String getImageDataElement() {
        return "span";
    }
}
