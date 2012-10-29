package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import org.apache.commons.lang.StringUtils;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.StAXTransformingBodyProcessor;

public class PromoBoxXMLParser extends BaseXMLParser<PromoBoxData> implements XmlParser<PromoBoxData> {

    private static final String UUID_REGEX = "[\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}";
    private static final String PROMO_TYPE = "promo";
    private static final String PROMO_BOX = "promo-box";
    private static final String PROMO_TITLE = "promo-title";
    private static final String PROMO_HEADLINE = "promo-headline";
    private static final String PROMO_INTRO = "promo-intro";
    private static final String PROMO_LINK= "promo-link";
    private static final String PROMO_IMAGE= "promo-image";

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
        
        // populate image attributes
        String imageUuid = parseImageUuid(dataBean.getImageFileRef());
        boolean imageExists = bodyProcessingContext.imageExists(imageUuid);
        if(imageExists && !StringUtils.isBlank(imageUuid)) {
            dataBean.setImageType(PROMO_TYPE);
            dataBean.setImageHeight(bodyProcessingContext.getAttributeForImage("height", imageUuid));
            dataBean.setImageWidth(bodyProcessingContext.getAttributeForImage("width", imageUuid));
            dataBean.setImageAlt(bodyProcessingContext.getAttributeForImage("alt", imageUuid));
            dataBean.setImageUrl(bodyProcessingContext.getAttributeForImage("src", imageUuid));
            dataBean.setImageCaption(bodyProcessingContext.getAttributeForImage("caption", imageUuid));
            dataBean.setImageSource(bodyProcessingContext.getAttributeForImage("source", imageUuid));
            dataBean.setImageMediaType(bodyProcessingContext.getAttributeForImage("media-type", imageUuid));
        } else  {
            // Setting to an empty string so the bean knows that there isn't a valid image available
            dataBean.setImageFileRef(StringUtils.EMPTY);
        }
    }

    private String parseImageUuid(String imageFileRef) {
        if(!StringUtils.isBlank(imageFileRef)) {
            Pattern p = Pattern.compile(UUID_REGEX);
            Matcher m = p.matcher(imageFileRef);
    
            if (m.find()) {
                return m.group(0);
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
            promoBoxData.setImageFileRef(fileRef);
        }
    }
    
    @Override
    boolean doesTriggerElementContainAllDataNeeded() {
        return false;
    }
}
