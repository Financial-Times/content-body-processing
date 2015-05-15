package com.ft.content.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import org.apache.commons.lang.StringUtils;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.ImageAttribute;
import com.ft.content.bodyprocessing.xml.StAXTransformingBodyProcessor;

public class PromoBoxXMLParser extends BaseXMLParser<PromoBoxData> implements XmlParser<PromoBoxData> {

    private static final String UUIDPREFIX = "uuid=";
    private static final String PROMO_TYPE = "promo";
    private static final String PROMO_BOX = "promo-box";
    private static final String PROMO_TITLE = "promo-title";
    private static final String PROMO_HEADLINE = "promo-headline";
    private static final String PROMO_INTRO = "promo-intro";
    private static final String PROMO_LINK= "promo-link";
    private static final String PROMO_IMAGE= "promo-image";
    private static final String PROMO_MASTER_IMAGE = "web-master";
    private static final String PROMO_CLASS_ATTRIBUTE = "class";
    private static final String NUMBERS_COMPONENT_IDENTIFIER = "numbers-component";

    private StAXTransformingBodyProcessor stAXTransformingBodyProcessor;
    
    protected PromoBoxXMLParser(StAXTransformingBodyProcessor stAXTransformingBodyProcessor) {
        super(PROMO_BOX);
        notNull(stAXTransformingBodyProcessor, "The StAXTransformingBodyProcessor cannot be null.");
        this.stAXTransformingBodyProcessor = stAXTransformingBodyProcessor;
    }

    @Override
    public void transformFieldContentToStructuredFormat(PromoBoxData dataBean, BodyProcessingContext bodyProcessingContext) {
        // Transform raw strings
        dataBean.setTitle(transformRawContentToStructuredFormat(dataBean.getTitle(), bodyProcessingContext));
        dataBean.setHeadline(transformRawContentToStructuredFormat(dataBean.getHeadline(), bodyProcessingContext));
        dataBean.setIntro(transformRawContentToStructuredFormat(dataBean.getIntro(), bodyProcessingContext));
        dataBean.setLink(transformRawContentToStructuredFormat(dataBean.getLink(), bodyProcessingContext));
        
        transformImagesToStructuredFormat(dataBean, bodyProcessingContext);
    }
    
    private void transformImagesToStructuredFormat(PromoBoxData dataBean, BodyProcessingContext bodyProcessingContext) {
        for (PromoboxImageData promoboxImage : dataBean.getPromoboxImages()) {
            if (isMasterImage(promoboxImage)) {
                String masterImageUuidFromXml = parseImageUuid(promoboxImage.getMasterImageFileRef());
                List<String> formatsForMasterImage = bodyProcessingContext
                        .retrieveFormatsForMasterImage(masterImageUuidFromXml);
                for (String imageUuid : formatsForMasterImage) {
                    PromoboxImageData newImageFormat = dataBean.addImageToPromobox(imageUuid);
                    populateImageAttributes(newImageFormat, bodyProcessingContext, imageUuid);
                }
            } else {
                String imageUuidExtractedFromXML = parseImageUuid(promoboxImage.getImageFileRef());
                populateImageAttributes(promoboxImage, bodyProcessingContext, imageUuidExtractedFromXML);
            }
        }
    }

    private boolean isMasterImage(PromoboxImageData promoboxImage) {
        return promoboxImage.getMasterImageFileRef() != null;
    }

    private void populateImageAttributes(PromoboxImageData promoboxImage, BodyProcessingContext bodyProcessingContext, String imageUuidExtractedFromXML) {
        boolean imageExists = bodyProcessingContext.imageExists(imageUuidExtractedFromXML);
        if(imageExists && !StringUtils.isBlank(imageUuidExtractedFromXML)) {
            promoboxImage.setImageType(PROMO_TYPE);
            promoboxImage.setImageHeight(bodyProcessingContext.getAttributeForImage(ImageAttribute.HEIGHT, imageUuidExtractedFromXML));
            promoboxImage.setImageWidth(bodyProcessingContext.getAttributeForImage(ImageAttribute.WIDTH, imageUuidExtractedFromXML));
            promoboxImage.setImageAlt(bodyProcessingContext.getAttributeForImage(ImageAttribute.ALT, imageUuidExtractedFromXML));
            promoboxImage.setImageUrl(bodyProcessingContext.getAttributeForImage(ImageAttribute.SRC, imageUuidExtractedFromXML));
            promoboxImage.setImageCaption(bodyProcessingContext.getAttributeForImage(ImageAttribute.CAPTION, imageUuidExtractedFromXML));
            promoboxImage.setImageSource(bodyProcessingContext.getAttributeForImage(ImageAttribute.SOURCE, imageUuidExtractedFromXML));
            promoboxImage.setImageMediaType(bodyProcessingContext.getAttributeForImage(ImageAttribute.MEDIA_TYPE, imageUuidExtractedFromXML));
        } else  {
            // Setting to an empty string so the bean knows that there isn't a valid image available
            promoboxImage.setImageFileRef(StringUtils.EMPTY);
        }
    }

    private String parseImageUuid(String imageFileRef) {
        if(!StringUtils.isBlank(imageFileRef)) {
            int indexOfUuid= imageFileRef.indexOf(UUIDPREFIX);
            if (indexOfUuid != -1) {
                int startingIndex = indexOfUuid + UUIDPREFIX.length();
                return imageFileRef.substring(startingIndex).trim(); 
            }
        }
        return StringUtils.EMPTY;
    }

    private String transformRawContentToStructuredFormat(String unprocessedContent, BodyProcessingContext bodyProcessingContext) {
        if (!StringUtils.isBlank(unprocessedContent)) {
            return stAXTransformingBodyProcessor.process(unprocessedContent, bodyProcessingContext);
        }
        return null;
    }

    @Override
    PromoBoxData createDataBeanInstance() {
        return new PromoBoxData();
    }

    @Override
    public void populateBean(PromoBoxData promoBoxData, StartElement nextStartElement, XMLEventReader xmlEventReader)
            throws UnexpectedElementStructureException {
        
        if (isElementNamed(nextStartElement.getName(), PROMO_BOX)) {
            Attribute classAttribute = nextStartElement.getAttributeByName(new QName(PROMO_CLASS_ATTRIBUTE));
            if (isNumbersComponent(classAttribute)) {
                promoBoxData.setAsNumbersComponent();
            }
        } 
        if (isElementNamed(nextStartElement.getName(), PROMO_TITLE)) {
            promoBoxData.setTitle(parseRawContent(PROMO_TITLE, xmlEventReader));
        } 
        else if (isElementNamed(nextStartElement.getName(), PROMO_HEADLINE)) {
            promoBoxData.setHeadline(parseRawContent(PROMO_HEADLINE, xmlEventReader));
        } 
        else if (isElementNamed(nextStartElement.getName(), PROMO_INTRO)) {
            promoBoxData.setIntro(parseRawContent(PROMO_INTRO, xmlEventReader));
        } 
        else if (isElementNamed(nextStartElement.getName(), PROMO_LINK)) {
            promoBoxData.setLink(parseRawContent(PROMO_LINK, xmlEventReader));
        } 
        else if (isElementNamed(nextStartElement.getName(), PROMO_IMAGE)) {
            String fileRef = parseAttribute("fileref", nextStartElement);
            promoBoxData.addImageToPromobox(fileRef);  
        } else if (isElementNamed(nextStartElement.getName(), PROMO_MASTER_IMAGE)) {
            String fileRef = parseAttribute("fileref", nextStartElement);
            promoBoxData.addMasterImageToPromobox(fileRef);
        }
    }

    private boolean isNumbersComponent(Attribute classAttribute) {
        if (classAttribute == null) {
            return false;
        }
        if (classAttribute.getValue() == null) {
            return false;
        }
        return classAttribute.getValue().contains(NUMBERS_COMPONENT_IDENTIFIER);
    }
    
    @Override
    boolean doesTriggerElementContainAllDataNeeded() {
        return false;
    }
}
