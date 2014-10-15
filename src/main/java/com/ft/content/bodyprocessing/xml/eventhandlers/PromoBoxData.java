package com.ft.content.bodyprocessing.xml.eventhandlers;

import org.apache.commons.lang.StringUtils;

import com.ft.content.bodyprocessing.BodyProcessingException;
import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.NumbersComponent;
import com.ft.api.ucm.model.v1.NumbersComponentFields;
import com.ft.api.ucm.model.v1.PromoBox;
import com.ft.api.ucm.model.v1.PromoBoxFields;
import com.ft.api.ucm.model.v1.TypeBasedImage;
import com.ft.api.ucm.model.v1.TypeBasedImage.ImageType;

public class PromoBoxData extends BaseData implements AssetAware {

    private String title;
    private String headline;
    private String intro;
    private String link;

    private String imageUrl;
    private String imageType;
    private String imageHeight;
    private String imageWidth;
    private String imageAlt;
    private String imageFileRef;
    private String imageCaption;
    private String imageSource;
    private String imageMediaType;
    
    private boolean numbersComponent = false;
    
    public String getImageCaption() {
        return imageCaption;
    }

    public void setImageCaption(String imageCaption) {
        this.imageCaption = imageCaption;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getImageMediaType() {
        return imageMediaType;
    }

    public void setImageMediaType(String imageMediaType) {
        this.imageMediaType = imageMediaType;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageAlt(String imageAlt) {
        this.imageAlt = imageAlt;
    }
    
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getTitle() {
        return title;
    }

    public String getHeadline() {
        return headline;
    }

    public String getIntro() {
        return intro;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageType() {
        return imageType;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public String getImageFileRef() {
        return imageFileRef;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public void setImageFileRef(String imageFileRef) {
        this.imageFileRef = imageFileRef;
    }
    
    public boolean isNumbersComponent() {
        return numbersComponent;
    }
    
    public void setAsNumbersComponent() {
        this.numbersComponent = true;
    }
    
    @Override
    public boolean isAllRequiredDataPresent() {
        return isNumbersComponent() ? validateNumbersComponent() : validateNormalPromoBox();
    }
    
    private boolean validateNormalPromoBox() {
        return containsValidData(this.title) || containsValidData(this.imageFileRef)
                || containsValidData(this.headline) || containsValidData(this.intro)
                || containsValidData(this.link);
    }
    
    private boolean validateNumbersComponent() {
        return containsValidData(this.headline) || containsValidData(this.intro);
    }

    @Override
    public Asset getAsset() throws BodyProcessingException {
        if(this.isAllRequiredDataPresent()) {
            return isNumbersComponent() ? getNumbersComponentAsset() : getPromoBoxAsset();
        }
        throw new BodyProcessingException(GET_ASSET_NO_VALID_EXCEPTION_MESSAGE);
     }

    private Asset getNumbersComponentAsset() {
        NumbersComponent numbersComponent = new NumbersComponent();
        NumbersComponentFields numbersComponentFields = new NumbersComponentFields(nullIfEmpty(this.headline), nullIfEmpty(intro));
        numbersComponent.setFields(numbersComponentFields);
        return numbersComponent;
    }

    private Asset getPromoBoxAsset() {
        PromoBox promoBox = new PromoBox();
        TypeBasedImage promoImage = createPromoImage();
        
        PromoBoxFields fields = new PromoBoxFields(nullIfEmpty(this.title), nullIfEmpty(this.headline), 
                                                   nullIfEmpty(intro), nullIfEmpty(this.link), promoImage);
        promoBox.setFields(fields);
        return promoBox;
    }

   
    private TypeBasedImage createPromoImage() {
        if(!StringUtils.isBlank(this.imageUrl)) {
            Integer imageWidthAsInt = convertToInt(imageWidth);
            Integer imageHeightAsInt = convertToInt(imageHeight);
            return new TypeBasedImage(nullIfEmpty(imageUrl), ImageType.PROMO, nullIfEmpty(imageSource),  nullIfEmpty(imageAlt), 
                    nullIfEmpty(imageCaption), imageHeightAsInt, imageWidthAsInt, nullIfEmpty(imageMediaType));
        }
        return null;
    }

    private Integer convertToInt(String value) {
        String valueToConvert = nullIfEmpty(value);
        if(valueToConvert != null) {
            return Integer.parseInt(valueToConvert);
        }
        return null;
    }
}
