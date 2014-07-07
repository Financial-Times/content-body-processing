package com.ft.content.bodyprocessing;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class ImageAttributeTest {

    @Test
    public void testStringConversion() {
        assertThat(ImageAttribute.getByName("alt"), is(equalTo(ImageAttribute.ALT)));
        assertThat(ImageAttribute.getByName("caption"), is(equalTo(ImageAttribute.CAPTION)));
        assertThat(ImageAttribute.getByName("height"), is(equalTo(ImageAttribute.HEIGHT)));
        assertThat(ImageAttribute.getByName("width"), is(equalTo(ImageAttribute.WIDTH)));
        assertThat(ImageAttribute.getByName("media_type"), is(equalTo(ImageAttribute.MEDIA_TYPE)));
        assertThat(ImageAttribute.getByName("source"), is(equalTo(ImageAttribute.SOURCE)));
        assertThat(ImageAttribute.getByName("src"), is(equalTo(ImageAttribute.SRC)));
        assertThat(ImageAttribute.getByName("data-img-id"), is(equalTo(ImageAttribute.IMAGE_ID)));

        assertThat(ImageAttribute.getByName("doesNotExist"), is(nullValue()));
    }
}
