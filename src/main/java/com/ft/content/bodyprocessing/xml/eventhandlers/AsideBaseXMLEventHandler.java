package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import org.apache.commons.lang.StringUtils;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.content.bodyprocessing.writer.BodyWriter;
import com.ft.api.ucm.model.v1.Asset;

public abstract class AsideBaseXMLEventHandler<T extends AssetAware> extends BaseXMLEventHandler {

    private AsideElementWriter asideElementWriter;

    protected AsideBaseXMLEventHandler(AsideElementWriter asideElementWriter) {
        notNull(asideElementWriter, "AsideElementWriter cannot be null");
        this.asideElementWriter = asideElementWriter;
    }
    
    @Override
    public void handleStartElementEvent(StartElement startElement, XMLEventReader xmlEventReader, BodyWriter eventWriter,
            BodyProcessingContext bodyProcessingContext) throws XMLStreamException {

        // Confirm that the startEvent is of the correct type
        if (isElementOfCorrectType(startElement)) {

            // Parse the xml needed to create a bean
            T dataBean = parseElementData(startElement, xmlEventReader);

            // Add asset to the context and create the aside element if all required data is present
            if (dataBean.isAllRequiredDataPresent()) {
                // process raw data and add any assets to the context
                transformFieldContentToStructuredFormat(dataBean, bodyProcessingContext);
                
                // ensure that the mutated bean data is still valid for processing after the transform field content processing
                if(dataBean.isAllRequiredDataPresent()) {
                    // Get the asset and add it to the context
                    String assetName = addAssetToContextAndReturnAssetName(bodyProcessingContext, dataBean);
    
                    if(!StringUtils.isBlank(assetName)) {
                        // build aside element
                        asideElementWriter.writeAsideElement(eventWriter, assetName, getType(dataBean));
                    }
                }
            }
        } else {
            processFallBack(startElement, xmlEventReader, eventWriter, bodyProcessingContext);
        }

    }

	protected String addAssetToContextAndReturnAssetName(BodyProcessingContext bodyProcessingContext, T dataBean) {
	    Asset asset = dataBean.getAsset();
	    bodyProcessingContext.addAsset(asset);
        return asset.getName();
    }
	
    protected void processFallBack(StartElement startElement, XMLEventReader xmlEventReader, BodyWriter eventWriter, BodyProcessingContext bodyProcessingContext) throws BodyProcessingException, XMLStreamException {
		throw new BodyProcessingException("event must correspond to" + getElementName() + " tag");
	}

    // Return the name of the start element - the element that the extending class is registered with
    abstract String getElementName();

    // Return the type that will be used to render the aside element
    abstract String getType(T dataBean);

    // Transform the data bean's contents as they can contain html that needs transforming. Any assets as part of the transformation are added to the context
    abstract void transformFieldContentToStructuredFormat(T dataBean, BodyProcessingContext bodyProcessingContext);

    // Parse and populate a data bean
    abstract T parseElementData(StartElement startElement, XMLEventReader xmlEventReader) throws XMLStreamException;
    
    protected boolean isElementOfCorrectType(StartElement event) {
        return event.getName().getLocalPart().toLowerCase().equals(getElementName().toLowerCase());
    }
}
