package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;

public class AsideElementWriter {
    
    protected static final QName P_TAG_QNAME = QName.valueOf("p");
    protected static final QName ASIDE_TAG_QNAME = QName.valueOf("aside");
    protected static final String DATA_ASSET_NAME_NAME = "data-asset-name";
    protected static final String DATA_ASSET_TYPE_NAME = "data-asset-type";
    
    @Deprecated
    public void writeAsideElement(BodyWriter bodyWriter, String name, String type, boolean needsPTag) {
        if (needsPTag) {
            writeEndTag(bodyWriter, P_TAG_QNAME);
        }
        
        Map<String, String> validAttributes  = new HashMap<String, String>();
        validAttributes.put(DATA_ASSET_NAME_NAME, name);
        validAttributes.put(DATA_ASSET_TYPE_NAME, type);
        
        writeStartTag(bodyWriter, ASIDE_TAG_QNAME, validAttributes);
        writeEndTag(bodyWriter, ASIDE_TAG_QNAME);
        
        if (needsPTag) {
            writeStartTag(bodyWriter, P_TAG_QNAME, new HashMap<String, String>());
        }
    }

    private void writeStartTag(BodyWriter eventWriter, QName pTagQname, Map<String, String> attributes) {
        eventWriter.writeStartTag(pTagQname.getLocalPart(), attributes);
    }

    private void writeEndTag(BodyWriter eventWriter, QName pTagQname) {
        eventWriter.writeEndTag(pTagQname.getLocalPart());
    }

    public void writeAsideElement(BodyWriter bodyWriter, String name, String type) {
        boolean needsPTag = bodyWriter.isPTagCurrentlyOpen();
        writeAsideElement(bodyWriter, name, type, needsPTag);
        
    }
}
