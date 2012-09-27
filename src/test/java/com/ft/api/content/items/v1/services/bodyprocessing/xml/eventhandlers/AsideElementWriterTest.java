package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers.AsideElementWriter;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AsideElementWriterTest {
    
    @Mock
    private BodyWriter mockEventWriter;
    
    @SuppressWarnings("unchecked")
    @Test
    public void shouldBuildAsideElementWithP() {
        String name = "someName";
        String type = "someType";
        
        ArgumentCaptor<Map> attributesCaptorAside = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map> attributesCaptorP = ArgumentCaptor.forClass(Map.class);
        
        AsideElementWriter asideElementBuilder = new AsideElementWriter();
        asideElementBuilder.writeAsideElement(mockEventWriter, name, type, true);
        
        verify(mockEventWriter, times(1)).writeStartTag(Mockito.eq("aside"), attributesCaptorAside.capture());
        verify(mockEventWriter, times(1)).writeStartTag(Mockito.eq("p"), attributesCaptorP.capture());
        verify(mockEventWriter, times(1)).writeEndTag(Mockito.eq("aside"));
        verify(mockEventWriter, times(1)).writeEndTag(Mockito.eq("p"));
        
        String actualAssetName = (String)attributesCaptorAside.getValue().get(AsideElementWriter.DATA_ASSET_NAME_NAME);
        Assert.assertEquals(name, actualAssetName);
        
        String actualAssetType = (String)attributesCaptorAside.getValue().get(AsideElementWriter.DATA_ASSET_TYPE_NAME);
        Assert.assertEquals(type, actualAssetType);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void shouldBuildAsideElementWithoutP() {
        String name = "someName";
        String type = "someType";
        
        ArgumentCaptor<Map> attributesCaptorAside = ArgumentCaptor.forClass(Map.class);
        
        AsideElementWriter asideElementBuilder = new AsideElementWriter();
        asideElementBuilder.writeAsideElement(mockEventWriter, name, type, false);
        
        verify(mockEventWriter, times(1)).writeStartTag(Mockito.eq("aside"), attributesCaptorAside.capture());
        verify(mockEventWriter, times(1)).writeEndTag(Mockito.eq("aside"));
        
        String actualAssetName = (String)attributesCaptorAside.getValue().get(AsideElementWriter.DATA_ASSET_NAME_NAME);
        Assert.assertEquals(name, actualAssetName);
        
        String actualAssetType = (String)attributesCaptorAside.getValue().get(AsideElementWriter.DATA_ASSET_TYPE_NAME);
        Assert.assertEquals(type, actualAssetType);
    }
}
