package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.unifiedContentModel.model.Asset;

@RunWith(MockitoJUnitRunner.class)
public class SlideshowXMLEventHandlerTest {

    private SlideshowXMLEventHandler slideshowXMLEventHandler;
    @Mock private SlideshowXMLParser mockSlideshowXMLParser;
    @Mock private AsideElementWriter mockAsideElementWriter;
    @Mock private XMLEventHandler mockFallbackEventHandler;
    @Mock private BodyProcessingContext mockBodyProcessingContext;
    @Mock private SlideshowData mockSlideshowData;
    @Mock private Asset mockAsset;
    @Mock private StartElement mockStartElement;
    @Mock private XMLEventReader mockXmlEventReader;
    @Mock private BodyWriter mockBodyWriter;
    @Mock private Attribute mockAttribute;
    @Mock private EndElement mockEndElement;
    
    private String UUID = "someUuid";
    private String assetName = "someAssetName";
                
    @Before
    public void setUp() {
        slideshowXMLEventHandler = new SlideshowXMLEventHandler(mockSlideshowXMLParser, mockAsideElementWriter, mockFallbackEventHandler);
    }
    
    @Test
    public void testGetElementName() {
        String actualElementName = slideshowXMLEventHandler.getElementName();
        assertEquals("a", actualElementName);
    }
    
    @Test
    public void testGetType() {
        String actualType = slideshowXMLEventHandler.getType();
        assertEquals("slideshow", actualType);
    }
    
    @Test
    public void testAddAssetToContextAndReturnAssetName() {
        when(mockSlideshowData.getUuid()).thenReturn(UUID);
        when(mockBodyProcessingContext.assignAssetNameToExistingAsset(UUID)).thenReturn(mockAsset);
        when(mockAsset.getName()).thenReturn(assetName);
        
        String actualAssetName = slideshowXMLEventHandler.addAssetToContextAndReturnAssetName(mockBodyProcessingContext, mockSlideshowData);
        assertNotNull(actualAssetName);
    }
    
    @Test
    public void testAddAssetToContextAndReturnAssetNameWhenAssetIdNotFound() {
        when(mockSlideshowData.getUuid()).thenReturn(UUID);
        when(mockBodyProcessingContext.assignAssetNameToExistingAsset(UUID)).thenReturn(null);
        
        String actualAssetName = slideshowXMLEventHandler.addAssetToContextAndReturnAssetName(mockBodyProcessingContext, mockSlideshowData);
        assertNull(actualAssetName);
    }
    
    @Test
    public void testParseElementData() throws XMLStreamException {
        slideshowXMLEventHandler.parseElementData(mockStartElement, mockXmlEventReader);
        verify(mockSlideshowXMLParser).parseElementData(mockStartElement, mockXmlEventReader);
    }
    
    @Test
    public void testProcessFallBack() throws XMLStreamException {
        slideshowXMLEventHandler.processFallBack(mockStartElement, mockXmlEventReader, mockBodyWriter, mockBodyProcessingContext);
        verify(mockFallbackEventHandler).handleStartElementEvent(mockStartElement, mockXmlEventReader, mockBodyWriter, mockBodyProcessingContext);
    }
    
    @Test
    public void testIsElementOfCorrectTypeTrue() {
        when(mockStartElement.getAttributeByName(QName.valueOf("type"))).thenReturn(mockAttribute);
        when(mockAttribute.getValue()).thenReturn("slideshow");
        assertTrue(slideshowXMLEventHandler.isElementOfCorrectType(mockStartElement));
    }
    
    @Test
    public void testIsElementOfCorrectTypeNoAttributePresent() {
        when(mockStartElement.getAttributeByName(QName.valueOf("type"))).thenReturn(null);
        assertFalse(slideshowXMLEventHandler.isElementOfCorrectType(mockStartElement));
    }
    
    @Test
    public void testIsElementOfCorrectTypeDifferentAttribute() {
        when(mockStartElement.getAttributeByName(QName.valueOf("type"))).thenReturn(mockAttribute);
        when(mockAttribute.getValue()).thenReturn("someOtherAttribute");
        assertFalse(slideshowXMLEventHandler.isElementOfCorrectType(mockStartElement));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithXMLParserNull() {
        new SlideshowXMLEventHandler(null, mockAsideElementWriter, mockFallbackEventHandler);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithAsideWriterNull() {
        new SlideshowXMLEventHandler(mockSlideshowXMLParser, null, mockFallbackEventHandler);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithFallbackHandlerNull() {
        new SlideshowXMLEventHandler(mockSlideshowXMLParser, mockAsideElementWriter, null);
    }
}
