package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.BodyProcessor;
import com.ft.content.bodyprocessing.ImageAttribute;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.ImmutableMap;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Test;
import org.mockito.Mock;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public abstract class BlogPostInlineImageDataEventHandlerTest extends BaseXMLEventHandlerTest {
    @Mock
    protected BodyWriter mockEventWriter;
    @Mock
    protected XMLEventReader2 mockXmlEventReader;
    @Mock
    protected BodyProcessingContext mockBodyProcessingContext;
    @Mock
    protected XMLEventHandler mockFallbackXmlEventHandler;
    @Mock
    protected BodyProcessor mockTransformingBodyProcessor;
    protected StartElement startElement;
    protected EndElement endElement;
    protected Characters characters;

    @Test
    public void testShouldFallbackOnEndEvent() throws Exception {
        endElement = getEndElement(getImageDataElement());
        getBlogPostInlineImageDataEventHandler().handleEndElementEvent(endElement, mockXmlEventReader, mockEventWriter);
        verify(mockFallbackXmlEventHandler).handleEndElementEvent(endElement, mockXmlEventReader, mockEventWriter);
    }

    @Test
    public void testShouldFallbackWhenStartElementIsNotP() throws Exception {
        startElement = getStartElement("div");
        getBlogPostInlineImageDataEventHandler().handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
        verify(mockFallbackXmlEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
    }

    @Test
    public void testShouldFallbackWhenStartElementHasNoImageIdAttribute() throws Exception {
        startElement = getStartElementWithAttributes(getImageDataElement(), ImmutableMap.of("someAttr", "someValue"));
        getBlogPostInlineImageDataEventHandler().handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
        verify(mockFallbackXmlEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
    }

    protected abstract String getImageDataElement();

    @SuppressWarnings("unchecked")
    @Test
    public void testEntireElementShouldBeRemovedIfImageIdAttributePresent() throws Exception {
        startElement = getStartElementWithAttributes(getImageDataElement(), ImmutableMap.of(ImageAttribute.IMAGE_ID.getAttributeName(), "someId"));
        characters = getCharacters("caption");
        endElement = getEndElement(getImageDataElement());
        when(mockXmlEventReader.hasNext()).thenReturn(true);
        when(mockXmlEventReader.nextEvent()).thenReturn(characters).thenReturn(endElement);

        getBlogPostInlineImageDataEventHandler().handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockEventWriter, never()).writeStartTag(eq(startElement.getName().getLocalPart()), anyMap());
        verify(mockXmlEventReader, times(2)).nextEvent();
    }

    public abstract BaseXMLEventHandler getBlogPostInlineImageDataEventHandler();
}
