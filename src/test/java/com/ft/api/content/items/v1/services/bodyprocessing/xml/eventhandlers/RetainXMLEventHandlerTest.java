package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers.RetainXMLEventHandler;
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
public class RetainXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	private RetainXMLEventHandler eventHandler;
	
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyWriter eventWriter;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	
	@Before
	public void setup() throws Exception {
		eventHandler = new RetainXMLEventHandler();
		Characters text = getCharacters("text");
		when(mockXmlEventReader.peek()).thenReturn(text);
		when(mockXmlEventReader.next()).thenReturn(text);
	}
	
	@Test
	public void startElementShouldBeOutput() throws Exception {
		ImmutableMap<String,String> emptyMap = ImmutableMap.of();
		StartElement startElement = getStartElement("a");
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), emptyMap);
	}
	
	@Test
	public void startElementShouldBeOutputWithAllAttributes() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("key", "value", "key2", "value2");
		StartElement startElement = getStartElementWithAttributes("a", attributesMap);
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).writeStartTag(startElement.getName().getLocalPart(), attributesMap);
	}
	
	@Test
	public void endElementShouldBeOutput() throws Exception {
		EndElement endElement = getEndElement("a");
		eventHandler.handleEndElementEvent(endElement, mockXmlEventReader, eventWriter);
		verify(eventWriter).writeEndTag(endElement.getName().getLocalPart());
	}
}

