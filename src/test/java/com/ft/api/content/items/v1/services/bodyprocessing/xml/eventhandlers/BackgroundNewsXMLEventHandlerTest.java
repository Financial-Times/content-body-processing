package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.StAXTransformingBodyProcessor;
import com.ft.unifiedContentModel.model.Asset;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BackgroundNewsXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	@Mock private BodyWriter mockEventWriter;
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	@Mock private Asset mockAsset;
	@Mock private BackgroundNewsXMLParser mockBackgroundNewsXMLParser;
	@Mock private AsideElementWriter mockAsideElementWriter;
	@Mock private BackgroundNewsData mockBackgroundNewsData;
	@Mock private StAXTransformingBodyProcessor mockStAXTransformingBodyProcessor;

	private StartElement startElement;
	private BaseXMLEventHandler backgroundNewsXMLEventHandler;


	@Before
	public void setup() throws XMLStreamException {
	  backgroundNewsXMLEventHandler = new BackgroundNewsXMLEventHandler(mockBackgroundNewsXMLParser, mockAsideElementWriter);
	  when(mockBackgroundNewsXMLParser.parseElementData(mockXmlEventReader)).thenReturn(mockBackgroundNewsData);
	}

	@Test(expected=XMLStreamException.class)
	public void failStartElementNotBackgroundNews() throws XMLStreamException {
	  startElement = getStartElement("p");
	  backgroundNewsXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
	}

	@Test
    public void shouldPassStartElementIfValidTag() throws XMLStreamException{
        String elementName = "asset1";
        Map<String,String> attributes =  new HashMap<String,String>();
        startElement = getStartElementWithAttributes("web-background-news", attributes);

        when(mockBackgroundNewsData.isOkToRender()).thenReturn(true);
        when(mockBackgroundNewsData.getAsset()).thenReturn(mockAsset);
        when(mockAsset.getName()).thenReturn(elementName);
        when(mockBodyProcessingContext.addAsset(Mockito.isA(Asset.class))).thenReturn(mockAsset);

        backgroundNewsXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

        verify(mockAsideElementWriter).writeAsideElement(mockEventWriter, elementName, "backgroundNews", true);
        verify(mockBackgroundNewsXMLParser).transformFieldContentToStructuredFormat(mockBackgroundNewsData, mockBodyProcessingContext);
    }

	@Test
   public void shouldNotWriteTheAsideTag() throws XMLStreamException {
       String elementName = "someElementName";
       Map<String,String> attributes =  new HashMap<String,String>();
       startElement = getStartElementWithAttributes("web-background-news", attributes);

       when(mockBackgroundNewsData.isOkToRender()).thenReturn(false);
       when(mockAsset.getName()).thenReturn(elementName);

		backgroundNewsXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

       verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "backgroundNews", true);
       verify(mockBackgroundNewsXMLParser, never()).transformFieldContentToStructuredFormat(mockBackgroundNewsData, mockBodyProcessingContext);
   }

	@Test
   public void shouldNotWriteTheAsideTagMissingSourceElement() throws XMLStreamException {
       String elementName = "someElementName";
       Map<String,String> attributes =  new HashMap<String,String>();
       startElement = getStartElementWithAttributes("web-background-news", attributes);

       when(mockBackgroundNewsData.getHeader()).thenReturn(null);
       String quoteText = "some text";
       when(mockBackgroundNewsData.getText()).thenReturn(quoteText);
       when(mockBackgroundNewsData.isOkToRender()).thenReturn(false);
       when(mockAsset.getName()).thenReturn(elementName);

		backgroundNewsXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

       verify(mockAsideElementWriter, never()).writeAsideElement(mockEventWriter, elementName, "backgroundNews", true);
       verify(mockStAXTransformingBodyProcessor, never()).process(quoteText, mockBodyProcessingContext);
       verify(mockBackgroundNewsXMLParser, never()).transformFieldContentToStructuredFormat(mockBackgroundNewsData, mockBodyProcessingContext);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testWithNullBackgroundNewsXMLParser() {
       new BackgroundNewsXMLEventHandler(null, mockAsideElementWriter);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testWithNullAsideElementWriter() {
       new BackgroundNewsXMLEventHandler(mockBackgroundNewsXMLParser, null);
   }

}