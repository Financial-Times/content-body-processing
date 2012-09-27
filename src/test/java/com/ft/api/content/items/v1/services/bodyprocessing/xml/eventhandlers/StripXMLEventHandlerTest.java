package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.verifyZeroInteractions;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers.StripXMLEventHandler;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(value=MockitoJUnitRunner.class)
public class StripXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	private StripXMLEventHandler eventHandler;
	
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyWriter eventWriter;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	
	@Before
	public void setup() {
		eventHandler = new StripXMLEventHandler();
	}
	
	@Test
	public void charactersShouldNotBeOutput() throws Exception {
		eventHandler.handleCharactersEvent(getCharacters("test"), mockXmlEventReader, eventWriter);
		verifyZeroInteractions(mockXmlEventReader, eventWriter);
	}
	
	@Test
	public void startElementShouldNotBeOutput() throws Exception {
		eventHandler.handleStartElementEvent(getStartElement("a"), mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		verifyZeroInteractions(mockXmlEventReader, eventWriter);
	}
	
	@Test
	public void endElementShouldNotBeOutput() throws Exception {
		eventHandler.handleEndElementEvent(getEndElement("a"), mockXmlEventReader, eventWriter);
		verifyZeroInteractions(mockXmlEventReader, eventWriter);
	}

}
