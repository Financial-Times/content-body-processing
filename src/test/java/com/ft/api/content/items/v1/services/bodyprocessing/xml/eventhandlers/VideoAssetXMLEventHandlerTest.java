package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers.AssetManager;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers.VideoAssetXMLEventHandler;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers.XMLEventHandler;
import com.ft.unifiedContentModel.model.Asset;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VideoAssetXMLEventHandlerTest extends BaseXMLEventHandlerTest {

		@Mock
		private BodyWriter mockEventWriter;
		@Mock private XMLEventReader2 mockXmlEventReader;
		@Mock private BodyProcessingContext mockBodyProcessingContext;
		@Mock private XMLEventHandler mockFallbackXmlEventHandler;
		@Mock private AssetManager assetManageable;
		@Mock private Asset mockAsset;

		private StartElement startElement;

		private VideoAssetXMLEventHandler videoAssetXMLEventHandler;



		@Before
		public void setup() {
			videoAssetXMLEventHandler = new VideoAssetXMLEventHandler(mockFallbackXmlEventHandler, assetManageable);
		}

		@Test(expected=XMLStreamException.class)
		public void failStartElementNotVideoObject() throws XMLStreamException{
			startElement = getStartElement("p");
			videoAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		}

		@SuppressWarnings({"unchecked","rawtypes", "serial"})
		@Test
		public void shouldPassStartElementIfValidTag() throws XMLStreamException{

			Map<String,String> attributes =  new HashMap<String,String>(){{
				put("videoID", "123456");
			}};
			startElement = getStartElementWithAttributes("videoPlayer", attributes);
			ArgumentCaptor<Map> attributesCaptor = ArgumentCaptor.forClass(Map.class);
			ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);

			when(assetManageable.createValidAttributesForMediaAsset(startElement, mockBodyProcessingContext, "video")).thenReturn(mockAsset);
			when(mockAsset.getName()).thenReturn("assetX");

			videoAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

			verify(mockFallbackXmlEventHandler, Mockito.never()).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
			verify(mockEventWriter).writeStartTag(tagCaptor.capture(), attributesCaptor.capture());
			assertEquals("aside", tagCaptor.getValue());
			Map<String,String> capturedAttributes = attributesCaptor.getValue();
			assertEquals("assetX", capturedAttributes.get("data-asset-name"));
			assertEquals("video", capturedAttributes.get("data-asset-type"));
		}
}
