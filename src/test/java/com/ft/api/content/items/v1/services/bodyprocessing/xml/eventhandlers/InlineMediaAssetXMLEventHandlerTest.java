package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.api.ucm.model.v1.Asset;


@RunWith(MockitoJUnitRunner.class)
public class InlineMediaAssetXMLEventHandlerTest extends BaseXMLEventHandlerTest {

	private static final QName INLINE_DWC_TAG_QNAME = QName.valueOf("inlineDwc");
	private static final String MEDIA_ASSET_HREF = "http://www.ft.com/blah/abc.xml?a=1&uuid=1234567890&b=3";
	private static final String NAME_1 = "name1";
	private static final String HREF_ATTRIBUTE_NAME = "href";
	private static final String TYPE_ATTRIBUTE_NAME = "type";
	private static final String SLIDESHOW_ATTRIBUTE_VALUE = "slideshow";
	
	@Mock private BodyWriter mockEventWriter;
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	@Mock private XMLEventHandler mockFallbackXmlEventHandler;
	@Mock private Asset mockAsset;
	
	private StartElement startElement;
	
	private InlineMediaAssetXMLEventHandler inlineMediaAssetXMLEventHandler;
	
	
	@Before
	public void setup() {
		inlineMediaAssetXMLEventHandler = new InlineMediaAssetXMLEventHandler(mockFallbackXmlEventHandler);
	}
	
	@Test(expected=XMLStreamException.class)
	public void failStartElementNotInlineDwc() throws XMLStreamException{
		startElement = getStartElement("p");
		inlineMediaAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void shouldFallbackStartElementChildNotA() throws XMLStreamException{
		startElement = getStartElement(INLINE_DWC_TAG_QNAME.getLocalPart());
		when(mockXmlEventReader.peek()).thenReturn(getStartElement("p"));
		inlineMediaAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockFallbackXmlEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockEventWriter, Mockito.never()).writeStartTag(Mockito.anyString(), Mockito.anyMap());
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void shouldFallbackStartElementChildAWithoutHref() throws XMLStreamException{
		startElement = getStartElement(INLINE_DWC_TAG_QNAME.getLocalPart());
		when(mockXmlEventReader.peek()).thenReturn(getStartElement("a"));
		inlineMediaAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockFallbackXmlEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockEventWriter, Mockito.never()).writeStartTag(Mockito.anyString(), Mockito.anyMap());
	}
	
	
	@Test
	@SuppressWarnings("unchecked")
	public void shouldFallbackStartElementChildAWithHrefWithoutUuid() throws XMLStreamException{
		startElement = getStartElement(INLINE_DWC_TAG_QNAME.getLocalPart());
		@SuppressWarnings("serial")
		Map<String,String> attributes = new HashMap<String,String>(){{put("href", "a?a=1&b=1");}};
		when(mockXmlEventReader.peek()).thenReturn(getStartElementWithAttributes("a", attributes));
		inlineMediaAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockFallbackXmlEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockEventWriter, Mockito.never()).writeStartTag(Mockito.anyString(), Mockito.anyMap());
	}
	
	@Test
	@SuppressWarnings({"unchecked", "serial"})
	public void shouldFallbackStartElementChildAWithHrefWithInvalidUuid() throws XMLStreamException{
		when(mockBodyProcessingContext.assignAssetNameToExistingAsset("1234567890")).thenReturn(null);
		Map<String,String> attributes =  new HashMap<String,String>(){{	
			put(HREF_ATTRIBUTE_NAME, MEDIA_ASSET_HREF); 
			put(TYPE_ATTRIBUTE_NAME, SLIDESHOW_ATTRIBUTE_VALUE);
		}};
		startElement = getStartElement(INLINE_DWC_TAG_QNAME.getLocalPart());
		when(mockXmlEventReader.peek()).thenReturn(getStartElementWithAttributes("a", attributes));
		inlineMediaAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockFallbackXmlEventHandler).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockEventWriter, Mockito.never()).writeStartTag(Mockito.anyString(), Mockito.anyMap());
	}
	
	
	
	@SuppressWarnings({"serial"})
	@Test
	public void shouldPassStartElementIfValidTag() throws XMLStreamException{
		when(mockBodyProcessingContext.assignAssetNameToExistingAsset("1234567890")).thenReturn(mockAsset);
		when(mockAsset.getName()).thenReturn(NAME_1);
		Map<String,String> attributes =  new HashMap<String,String>(){{	
			put(HREF_ATTRIBUTE_NAME, MEDIA_ASSET_HREF); 
		}};
		when(mockXmlEventReader.peek()).thenReturn(getStartElementWithAttributes("a", attributes));
		startElement = getStartElement(INLINE_DWC_TAG_QNAME.getLocalPart());
		
		inlineMediaAssetXMLEventHandler.handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);

		verify(mockFallbackXmlEventHandler, Mockito.never()).handleStartElementEvent(startElement, mockXmlEventReader, mockEventWriter, mockBodyProcessingContext);
		verify(mockBodyProcessingContext).assignAssetNameToExistingAsset("1234567890");
	}
	
}
