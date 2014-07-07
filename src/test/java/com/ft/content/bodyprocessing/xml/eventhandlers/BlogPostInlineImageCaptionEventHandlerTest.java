package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.ImageAttribute;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.ImmutableMap;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BlogPostInlineImageCaptionEventHandlerTest extends BaseXMLEventHandlerTest {
    @Mock
    private BodyWriter mockEventWriter;
    @Mock
    private XMLEventReader2 mockXmlEventReader;
    @Mock
    private BodyProcessingContext mockBodyProcessingContext;
    @Mock
    private XMLEventHandler mockFallbackXmlEventHandler;

    private StartElement startElement;
    private EndElement endElement;
    private Characters characters;

    private BlogPostInlineImageCaptionEventHandler blogPostInlineImageCaptionEventHandler;

    @Before
    public void setUp() throws Exception {
        blogPostInlineImageCaptionEventHandler = new BlogPostInlineImageCaptionEventHandler(mockFallbackXmlEventHandler);
    }

    @Test
    public void testShouldFallbackOnEndEvent() throws Exception {
        endElement = getEndElement("p");
        blogPostInlineImageCaptionEventHandler.handleEndElementEvent(endElement, mockXmlEventReader, mockEventWriter);
        verify(mockFallbackXmlEventHandler).handleEndElementEvent(endElement, mockXmlEventReader, mockEventWriter);
    }

    @Test
    public void testShouldFallbackWhenStartElementIsNotP() throws Exception {
        startElement = getStartElement("div");
        blogPostInlineImageCaptionEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
        verify(mockFallbackXmlEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
    }

    @Test
    public void testShouldFallbackWhenStartElementHasNoImageIdAttribute() throws Exception {
        startElement = getStartElementWithAttributes("p", ImmutableMap.of("someAttr", "someValue"));
        blogPostInlineImageCaptionEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
        verify(mockFallbackXmlEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEntireElementShouldBeRemovedIfImageIdAttributePresent() throws Exception {
        startElement = getStartElementWithAttributes("p", ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("caption");
        endElement = getEndElement("p");
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenReturn(characters).thenReturn(endElement);

        blogPostInlineImageCaptionEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockEventWriter, never()).writeStartTag(eq(startElement.getName().getLocalPart()), anyMap());
        verify(mockXmlEventReader, times(2)).nextEvent();
    }

    @Test
    public void testCaptionShouldBeAddedToTheContext() throws Exception {
        startElement = getStartElementWithAttributes("p", ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("caption");
        endElement = getEndElement("p");
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenReturn(characters).thenReturn(endElement);

        blogPostInlineImageCaptionEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockBodyProcessingContext).addAttributesToExistingImageWithId(eq("someId"), eq(ImmutableMap.of(ImageAttribute.CAPTION.getAttributeName(), "caption")));
    }

    @Test
    public void testCaptionShouldNotBeAddedToTheContextWhenXMLStreamExceptionThrown() throws Exception {
        startElement = getStartElementWithAttributes("p", ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("caption");
        endElement = getEndElement("p");
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenThrow(new XMLStreamException());

        blogPostInlineImageCaptionEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verifyZeroInteractions(mockBodyProcessingContext);
    }

    @Test
    public void testEmptyCaptionShouldNotBeAddedToTheContext() throws Exception {
        startElement = getStartElementWithAttributes("p", ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("");
        endElement = getEndElement("p");
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenReturn(characters).thenReturn(endElement);

        blogPostInlineImageCaptionEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verifyZeroInteractions(mockBodyProcessingContext);
    }
}
