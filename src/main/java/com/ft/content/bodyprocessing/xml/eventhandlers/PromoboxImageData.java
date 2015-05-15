package com.ft.content.bodyprocessing.xml.eventhandlers;

public class PromoboxImageData {
    private String imageUrl;
    private String imageType;
    private String imageHeight;
    private String imageWidth;
    private String imageAlt;
    private String imageFileRef;
    private String imageCaption;
    private String imageSource;
    private String imageMediaType;
    private String masterImageFileRef;

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

    public String getMasterImageFileRef() {
        return masterImageFileRef;
    }

    public void setMasterImageFileRef(String masterImageUid) {
        this.masterImageFileRef = masterImageUid;
    }
}