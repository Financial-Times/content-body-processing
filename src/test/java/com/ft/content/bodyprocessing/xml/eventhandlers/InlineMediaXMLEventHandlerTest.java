package com.ft.content.bodyprocessing.xml.eventhandlers;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.ft.api.ucm.model.v1.Asset;

@RunWith(MockitoJUnitRunner.class)
public class InlineMediaXMLEventHandlerTest {

    private InlineMediaXMLEventHandler inlineMediaXMLEventHandler;
    @Mock private SlideshowXMLParser mockSlideshowXMLParser;
    @Mock private AsideElementWriter mockAsideElementWriter;
    @Mock private XMLEventHandler mockFallbackEventHandler;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private SlideshowData mockSlideshowData;
    @Mock private Asset mockAsset;
    @Mock private StartElement mockStartElement;
    @Mock private XMLEventReader mockXmlEventReader;
    @Mock private BodyWriter mockBodyWriter;
    
    private String UUID = "someUuid";
    private String assetName = "someAssetName";
        
    @Before
    public void setUp() {
        inlineMediaXMLEventHandler = new InlineMediaXMLEventHandler(mockSlideshowXMLParser, mockAsideElementWriter, mockFallbackEventHandler);
    }
    
    @Test
    public void testGetElementName() {
        String actualElementName = inlineMediaXMLEventHandler.getElementName();
        assertEquals("inlineDwc", actualElementName);
    }
    
    @Test
    public void testGetType() {
        String actualType = inlineMediaXMLEventHandler.getType(null);
        assertEquals("slideshow", actualType);
    }
    
    @Test
    public void testAddAssetToContextAndReturnAssetName() {
        when(mockSlideshowData.getUuid()).thenReturn(UUID);
        when(mockBodyProcessingContext.assignAssetNameToExistingAsset(UUID)).thenReturn(mockAsset);
        when(mockAsset.getName()).thenReturn(assetName);
        
        String actualAssetName = inlineMediaXMLEventHandler.addAssetToContextAndReturnAssetName(mockBodyProcessingContext, mockSlideshowData);
        assertNotNull(actualAssetName);
    }
    
    @Test
    public void testAddAssetToContextAndReturnAssetNameWhenAssetIdNotFound() {
        when(mockSlideshowData.getUuid()).thenReturn(UUID);
        when(mockBodyProcessingContext.assignAssetNameToExistingAsset(UUID)).thenReturn(null);
        
        String actualAssetName = inlineMediaXMLEventHandler.addAssetToContextAndReturnAssetName(mockBodyProcessingContext, mockSlideshowData);
        assertNull(actualAssetName);
    }
    
    @Test
    public void testParseElementData() throws XMLStreamException {
        inlineMediaXMLEventHandler.parseElementData(mockStartElement, mockXmlEventReader);
        verify(mockSlideshowXMLParser).parseElementData(mockStartElement, mockXmlEventReader);
    }
    
    @Test
    public void testProcessFallBack() throws XMLStreamException {
        inlineMediaXMLEventHandler.processFallBack(mockStartElement, mockXmlEventReader, mockBodyWriter, mockBodyProcessingContext);
        verify(mockFallbackEventHandler).handleStartElementEvent(mockStartElement, mockXmlEventReader, mockBodyWriter, mockBodyProcessingContext);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithXMLParserNull() {
        new InlineMediaXMLEventHandler(null, mockAsideElementWriter, mockFallbackEventHandler);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithAsideWriterNull() {
        new InlineMediaXMLEventHandler(mockSlideshowXMLParser, null, mockFallbackEventHandler);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithFallbackHandlerNull() {
        new InlineMediaXMLEventHandler(mockSlideshowXMLParser, mockAsideElementWriter, null);
    }
}
