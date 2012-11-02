package com.ft.api.content.items.v1.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingException;
import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessor;
import com.ft.api.ucm.model.v1.Body;

@RunWith(value=MockitoJUnitRunner.class)
public class DefaultBodyTransformerTest {

	private static final String VALIDBODY = "body";
	private static final String NONVALIDBODY = "nonvalidbody";
	private static final String TRANSFORMEDBODY = "transformedbody" ;
    private static final String MEDIA_TYPE = "text/html" ;

	private DefaultBodyTransformer bodyTransformer;

	@Mock
	private BodyProcessor mockBodyProcessor;
	@Before
	public void setup() {
		bodyTransformer = new DefaultBodyTransformer(mockBodyProcessor, MEDIA_TYPE);
		when(mockBodyProcessor.process(contains(VALIDBODY), (BodyProcessingContext) isNull())).thenReturn("transformedbody");
		when(mockBodyProcessor.process(contains(NONVALIDBODY), (BodyProcessingContext) isNull())).thenThrow(new BodyProcessingException("Problem with processing"));
	}

	@Test
	public void shouldTransformValidXmlBodySuccessfully() {
		Body body = bodyTransformer.transform(VALIDBODY, null);
		assertEquals(TRANSFORMEDBODY, body.getBody());
        assertEquals(MEDIA_TYPE, body.getMediaType());
	}

	@Test
	public void shouldTransformNullBodySuccessfully() {
		Body body = bodyTransformer.transform(null, null);
		assertEquals(null, body.getBody());
        assertEquals(null, body.getMediaType());
	}

	@Test
	public void shouldTransformEmptyBodySuccessfully() {
		Body body = bodyTransformer.transform("", null);
		assertEquals(null, body.getBody());
		assertEquals(null, body.getMediaType());
	}

	@Test(expected = BodyProcessingException.class)
	public void shouldReturnNullWhenBodyIsInvalidXml() {
		bodyTransformer.transform(NONVALIDBODY, null);
	}

}
