package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.api.content.items.v1.services.bodyprocessing.ImageAttribute;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.ImmutableMap;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import org.codehaus.stax2.XMLEventReader2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(value=MockitoJUnitRunner.class)
public class ImageTransformXMLEventHandlerTest  extends BaseXMLEventHandlerTest {
	
	private ImageTransformXMLEventHandler eventHandler;

	@Mock private BodyWriter      eventWriter;
	@Mock private XMLEventReader2 mockXmlEventReader;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	
	private final static String FILEREF = "/FT%20Graphics/Online%20Graphics/Charts/2009/06%20/G062X-Rio-Tinto-thumb.jpg?uuid=e9f7ddf0-51f0-11de-b986-00144feabdc0";
	private final static String INVALIDFILEREF = "/FT%20Graphics/Online%20Graphics/Charts/2009/06%20/G062X-Rio-Tinto-thumb.jpg?e9f7ddf0-51f0-11de-b986-00144feabdc0";
	private final static String UUID = "e9f7ddf0-51f0-11de-b986-00144feabdc0";


	@Before
	public void setUp() {
		eventHandler = new ImageTransformXMLEventHandler("img", "class", "ft-web-inline-picture");
		
		when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.HEIGHT, UUID)).thenReturn("100");
		when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.WIDTH, UUID)).thenReturn("300");
		when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.SRC, UUID)).thenReturn("http://image.url");
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorShouldRejectEmptyReplacementElement() {
		new ImageTransformXMLEventHandler("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorShouldRejectNullReplacementElement() {
		new ImageTransformXMLEventHandler(null);
	}

	@Test
	public void startElementShouldBeTransformed() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("fileref", FILEREF, "align", "left", "margin-bottom", "0px", "alt", "text describing image");
		StartElement startElement = getStartElementWithAttributes("web-inline-picture", attributesMap);
		ImmutableMap<String,String> expectedMap = ImmutableMap.of("class", "ft-web-inline-picture", "src", "http://image.url", "height", "100", "width", "300", "alt", "text describing image");
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);

		verify(eventWriter).writeStartTag("img", expectedMap);
		verify(eventWriter).writeEndTag("img");
	}
	
	@Test
	public void startElementShouldBeTransformedMissingAltTagAdded() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("fileref", FILEREF, "align", "left", "margin-bottom", "0px");
		StartElement startElement = getStartElementWithAttributes("web-inline-picture", attributesMap);
		ImmutableMap<String,String> expectedMap = ImmutableMap.of("class", "ft-web-inline-picture", "src", "http://image.url", "height", "100", "width", "300", "alt", "");
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);

		verify(eventWriter).writeStartTag("img", expectedMap);
		verify(eventWriter).writeEndTag("img");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void entireElementShouldBeRemovedIfNoMatchingImageFound() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("fileref", FILEREF, "align", "left", "margin-bottom", "0px");
		StartElement startElement = getStartElementWithAttributes("web-inline-picture", attributesMap);
		when(mockBodyProcessingContext.getAttributeForImage(ImageAttribute.HEIGHT, UUID)).thenThrow(new BodyProcessingException("No matching image found"));
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);

		verify(eventWriter, never()).writeStartTag(eq("img"), anyMap());
		verify(mockXmlEventReader).hasNext();
	}

	@SuppressWarnings("unchecked")
	@Test 
	public void entireElementShouldBeRemovedIfNoUUIDSrcAttributePresent() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("notfileref", FILEREF, "align", "left", "margin-bottom", "0px");
		StartElement startElement = getStartElementWithAttributes("web-inline-picture", attributesMap);
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		
		verify(eventWriter, never()).writeStartTag(eq("img"), anyMap());
		verify(mockXmlEventReader).hasNext();
	}

	@SuppressWarnings("unchecked")
	@Test 
	public void entireElementShouldBeRemovedIfSrcAttributeDoesNotContainUUIDPrefix() throws Exception {
		ImmutableMap<String,String> attributesMap = ImmutableMap.of("fileref", INVALIDFILEREF, "align", "left", "margin-bottom", "0px");
		StartElement startElement = getStartElementWithAttributes("web-inline-picture", attributesMap);
		eventHandler.handleStartElementEvent(startElement, mockXmlEventReader, eventWriter, mockBodyProcessingContext);
		
		verify(eventWriter, never()).writeStartTag(eq("img"), anyMap());
		verify(mockXmlEventReader).hasNext();
	}

	@Test
	public void endElementShouldBeTransformed() throws Exception {
		EndElement endElement = getEndElement("web-inline-picture");

		eventHandler.handleEndElementEvent(endElement, mockXmlEventReader, eventWriter);

		verify(eventWriter).writeEndTag("img");
	}
}
