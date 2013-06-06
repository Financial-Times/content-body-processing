package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.api.ucm.model.v1.TypeBasedImage;
import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.ImageAttribute;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.stream.XMLEventReader;
import java.util.Map;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BlogPostImageRetainWithSpecificAttributesXMLEventHandlerTest extends BaseXMLEventHandlerTest {
    
    @Mock
    private BodyProcessingContext bodyProcessingContext;
    @Mock
    private BodyWriter eventWriter;
    @Mock
    private XMLEventReader eventReader;

    private BlogPostImageRetainWithSpecificAttributesXMLEventHandler
            blogPostImageRetainWithSpecificAttributesXMLEventHandler;

    @Before
    public void setUp() {
        blogPostImageRetainWithSpecificAttributesXMLEventHandler = new 
                BlogPostImageRetainWithSpecificAttributesXMLEventHandler();
    }

    @Test
    public void shouldAddProcessedImageAttributesAsImageToProcessingContext() throws Exception {
        Map<String, String> expectedImageAttributes = ImmutableMap
                .of(ImageAttribute.SRC.getAttributeName(), "someImage.jpg", ImageAttribute.ALT.getAttributeName(), "someAlt");

        blogPostImageRetainWithSpecificAttributesXMLEventHandler.handleStartElementEvent
                (getStartElementWithAttributes("img", expectedImageAttributes), eventReader,
                eventWriter, bodyProcessingContext);

        verify(bodyProcessingContext).addImageWithAttributes(expectedImageAttributes, TypeBasedImage.ImageType.INLINE_EXT);
        verify(eventWriter).writeStartTag("img", expectedImageAttributes);
    }
}
