package com.ft.content.bodyprocessing.xml.eventhandlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ft.api.ucm.model.v1.Asset;
import com.ft.api.ucm.model.v1.NumbersComponent;
import com.ft.api.ucm.model.v1.NumbersComponentFields;
import com.ft.api.ucm.model.v1.PromoBox;
import com.ft.api.ucm.model.v1.PromoBoxFields;
import com.ft.api.ucm.model.v1.TypeBasedImage;
import com.ft.api.ucm.model.v1.TypeBasedImage.ImageType;
import com.ft.content.bodyprocessing.BodyProcessingException;
import com.google.common.collect.Lists;

public class PromoBoxData extends BaseData implements AssetAware {

    private String title;
    private String headline;
    private String intro;
    private String link;
    
    private List<PromoboxImageData> promoboxImages;
    private boolean numbersComponent = false;
    
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
    
    public boolean isNumbersComponent() {
        return numbersComponent;
    }
    
    public void setAsNumbersComponent() {
        this.numbersComponent = true;
    }
    
    public List<PromoboxImageData> getPromoboxImages() {
        initializePromoboxImagesIfNull();
        return new ArrayList<PromoboxImageData>(promoboxImages);
    }
    
    public PromoboxImageData addImageToPromobox(String imageFileRef) {
        initializePromoboxImagesIfNull();
        
        PromoboxImageData promoboxImage = new PromoboxImageData();
        promoboxImage.setImageFileRef(imageFileRef);
        promoboxImages.add(promoboxImage);

        return promoboxImage;
    }

    public void addMasterImageToPromobox(String masterImageUuid) {
        initializePromoboxImagesIfNull();
        
        PromoboxImageData promoboxImage = new PromoboxImageData();
        promoboxImage.setMasterImageFileRef(masterImageUuid);

        promoboxImages.add(promoboxImage);
    }
    
    @Override
    public boolean isAllRequiredDataPresent() {
        return isNumbersComponent() ? validateNumbersComponent() : validateNormalPromoBox();
    }
    
    private boolean validateNormalPromoBox() {
        return containsValidData(this.title) || validateFileRefForImages()
                || containsValidData(this.headline) || containsValidData(this.intro)
                || containsValidData(this.link);
    }
    
    private boolean validateNumbersComponent() {
        return containsValidData(this.headline) || containsValidData(this.intro);
    }
    
    private boolean validateFileRefForImages() {
        if (this.promoboxImages == null) {
            return false;
        }
        
        for (PromoboxImageData imageData : this.promoboxImages) {
            if (!containsValidData(imageData.getImageFileRef())) {
                return false;
            }
        }

        return true;
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
        List<TypeBasedImage> promoboxAssetImages = Lists.newArrayList();

        if (this.promoboxImages != null) {
            for (PromoboxImageData promoboxImage : this.promoboxImages) {
                TypeBasedImage assetImage = createPromoImage(promoboxImage);
                if (assetImage != null) {
                    promoboxAssetImages.add(assetImage);
                }
            }
        }

        PromoBoxFields fields = new PromoBoxFields(nullIfEmpty(this.title), nullIfEmpty(this.headline),
                nullIfEmpty(intro), nullIfEmpty(this.link), nullIfEmpty(promoboxAssetImages));
        promoBox.setFields(fields);
        
        return promoBox;
    }
   
    private TypeBasedImage createPromoImage(PromoboxImageData promoboxImage) {
        if (!StringUtils.isBlank(promoboxImage.getImageUrl())) {
            Integer imageWidthAsInt = convertToInt(promoboxImage.getImageWidth());
            Integer imageHeightAsInt = convertToInt(promoboxImage.getImageHeight());
            return new TypeBasedImage(nullIfEmpty(promoboxImage.getImageUrl()), ImageType.PROMO,
                    nullIfEmpty(promoboxImage.getImageSource()), nullIfEmpty(promoboxImage.getImageAlt()),
                    nullIfEmpty(promoboxImage.getImageCaption()), imageHeightAsInt, imageWidthAsInt,
                    nullIfEmpty(promoboxImage.getImageMediaType()));
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
    
    private List<TypeBasedImage> nullIfEmpty(List<TypeBasedImage> list) {
        if (list == null) {
            return null;
        }
        
        if (list.size() == 0) {
            return null;
        }
        
        return list;
    }
    
    private void initializePromoboxImagesIfNull() {
        if (promoboxImages == null) {
            promoboxImages = Lists.newArrayList();
        }
    }
}
