package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.ImmutableMap;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(value=MockitoJUnitRunner.class)
public class LinkTagXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	private LinkTagXMLEventHandler eventHandler;
	
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyWriter eventWriter;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	
	@Before
	public void setup() throws Exception {
		eventHandler = new LinkTagXMLEventHandler();
		Characters text = getCharacters("text");
		when(mockXmlEventReader.peek()).thenReturn(text);
		when(mockXmlEventReader.next()).thenReturn(text);
	}
	
	@Test
	public void startElementShouldBeOutput() throws Exception {
		StartElement startElement = getStartElement("a");
		ImmutableMap<String,String> expectedMap = ImmutableMap.of();
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), expectedMap);
	}
	
	@Test
	public void startElementShouldBeOutputWithHrefAttributeIfPresent() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("key", "value", "href", "value2");
		ImmutableMap<String,String> expectedMap = ImmutableMap.of("href", "value2");
		
		StartElement startElement = getStartElementWithAttributes("a", attributesMap);
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), expectedMap);
	}
	
	@Test
	public void startElementShouldBeOutputWithHrefAttributeQueryParamsTransformedIfPresent() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("key", "value", "href", "mailto:a@b.com?su=something with spaces");
		ImmutableMap<String,String> expectedMap = ImmutableMap.of("href", "mailto:a@b.com?su=something%20with%20spaces");
		
		StartElement startElement = getStartElementWithAttributes("a", attributesMap);
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), expectedMap);
	}
	
	@Test
	public void startElementShouldBeOutputWithTitleAttributeIfPresent() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("key", "value", "title", "value2");
		ImmutableMap<String,String> expectedMap = ImmutableMap.of("title", "value2");
		
		StartElement startElement = getStartElementWithAttributes("a", attributesMap);
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), expectedMap);
	}
	
	@Test
	public void endElementShouldBeOutput() throws Exception {
		EndElement endElement = getEndElement("a");
		eventHandler.handleEndElementEvent(endElement, mockXmlEventReader, eventWriter);
		verify(eventWriter).writeEndTag(endElement.getName().getLocalPart());
	}

}

