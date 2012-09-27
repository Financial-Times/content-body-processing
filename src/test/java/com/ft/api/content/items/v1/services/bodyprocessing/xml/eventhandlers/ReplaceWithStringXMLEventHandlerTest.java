package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.verify;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers.ReplaceWithStringXMLEventHandler;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(value=MockitoJUnitRunner.class)
public class ReplaceWithStringXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	private ReplaceWithStringXMLEventHandler eventHandler;
	
	private EndElement endElement;
	private StartElement startElement;
	
	@Mock private BodyWriter eventWriter;
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	
	@Before
	public void setup() {
		eventHandler = new ReplaceWithStringXMLEventHandler("\n");
	}
	
	@Test
	public void endElementShouldBeReplacedWithSingleNewLine() throws Exception {
		endElement = getEndElement("p");
		eventHandler.handleEndElementEvent(endElement, mockXmlEventReader, eventWriter);
		verify(eventWriter).write("\n");
	}
	
	@Test
	public void startElementShouldBeReplacedWithSingleNewLine() throws Exception {
		startElement = getStartElement("p");
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verify(eventWriter).write("\n");
	}

}
