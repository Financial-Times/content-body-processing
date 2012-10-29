package com.ft.api.content.items.v1.services.bodyprocessing;

import org.junit.Test;

import junit.framework.Assert;


public class ImageAttributeTest {

    @Test
    public void testStringConversion() {
        Assert.assertEquals(ImageAttribute.ALT, ImageAttribute.getByName("alt"));
        Assert.assertEquals(ImageAttribute.CAPTION, ImageAttribute.getByName("caption"));
        Assert.assertEquals(ImageAttribute.HEIGHT, ImageAttribute.getByName("height"));
        Assert.assertEquals(ImageAttribute.WIDTH, ImageAttribute.getByName("width"));
        Assert.assertEquals(ImageAttribute.MEDIA_TYPE, ImageAttribute.getByName("media_type"));
        Assert.assertEquals(ImageAttribute.SOURCE, ImageAttribute.getByName("source"));
        Assert.assertEquals(ImageAttribute.SRC, ImageAttribute.getByName("src"));
        
        Assert.assertNull(ImageAttribute.getByName("doesNotExist"));
    }
}
