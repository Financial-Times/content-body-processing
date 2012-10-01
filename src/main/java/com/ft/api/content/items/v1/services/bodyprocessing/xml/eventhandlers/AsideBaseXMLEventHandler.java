package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.writer.BodyWriter;
import com.ft.unifiedContentModel.model.Asset;

public abstract class AsideBaseXMLEventHandler<T extends AssetAware> extends BaseXMLEventHandler {

    private AsideElementWriter asideElementWriter;

    protected AsideBaseXMLEventHandler(AsideElementWriter asideElementWriter) {
        notNull(asideElementWriter, "AsideElementWriter cannot be null");
        this.asideElementWriter = asideElementWriter;
    }
    
    @Override
    public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter,
            BodyProcessingContext bodyProcessingContext) throws XMLStreamException {

        // Confirm that the startEvent is a pull quote
        if (isElementOfCorrectType(event)) {

            // Parse the pull quote data
            T dataBean = parseElementData(xmlEventReader);

            // Add asset to the context and create the aside element if all required data is present
            if (dataBean.isOkToRender()) {
                // process raw data and add any assets to the context
                transformFieldContentToStructuredFormat(dataBean, bodyProcessingContext);
                
                // Get the asset and add it to the context
                String assetName = addAssetToContextAndReturnAssetName(bodyProcessingContext, dataBean.getAsset());

                // build aside element and skip until end of tag
                asideElementWriter.writeAsideElement(eventWriter, assetName, getType(), needPTag());
            }
        } else {
            throw new XMLStreamException("event must correspond to" + getElementName() + " tag");
        }

    }

    abstract boolean needPTag();

    abstract String getElementName();

    abstract String getType();

    abstract void transformFieldContentToStructuredFormat(T dataBean, BodyProcessingContext bodyProcessingContext);

    abstract T parseElementData(XMLEventReader xmlEventReader) throws XMLStreamException;

    private String addAssetToContextAndReturnAssetName(BodyProcessingContext bodyProcessingContext, Asset asset) {
        bodyProcessingContext.addAsset(asset);
        return asset.getName();
    }
    
    private boolean isElementOfCorrectType(StartElement event) {
        return event.getName().getLocalPart().toLowerCase().equals(getElementName().toLowerCase());
    }
}
