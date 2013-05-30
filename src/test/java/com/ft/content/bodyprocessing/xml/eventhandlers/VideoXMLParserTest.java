package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VideoXMLParserTest extends BaseXMLParserTest {
    
    private static final String VIDEO_ID = "79196309001";
    private XMLEventReader xmlEventReader;
    private String validXml = "<videoPlayer videoID=\"79196309001\"></videoPlayer>";
    
    
    private VideoXMLParser videoXMLParser;
    @Mock PullQuoteData mockPullQuoteData; 
    
    @Before
    public void setUp() {
        videoXMLParser = new VideoXMLParser();
    }
    
    @Test
    public void testParseChildElementData() throws XMLStreamException {
        xmlEventReader = createReaderForXml(validXml);
        StartElement startElement = getStartElement(xmlEventReader);
        VideoData videoData = videoXMLParser.parseElementData(startElement, xmlEventReader);
        
        assertNotNull("VideoData should not be null", videoData);
        assertEquals("Id was not as expected", VIDEO_ID, videoData.getId());
        assertTrue("xmlReader should have no more events", xmlEventReader.nextEvent().isEndDocument());
    }
    
    @Test
    public void testCreateBeanInstance() throws XMLStreamException {
        VideoData videoData = videoXMLParser.createDataBeanInstance();
        assertNotNull("VideoData should not be null", videoData);
    }

}
