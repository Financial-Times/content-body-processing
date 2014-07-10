package com.ft.content.bodyprocessing.transformer;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TagsTransformerTest {

	private TagsTransformer tobeTested;
	
	@Before
	public void setUp() throws Exception {
		tobeTested = new TagsTransformer();
	}
	
	@After
	public void tearDown() throws Exception{
		tobeTested = null;
	}
	
	@Test
	public void testTagRemoval() throws Exception{
		String expected = "test of tag removal";
		String str = "<?xml><block>"+ expected +"</block></xml>";
		
		String result = tobeTested.transform(str);
		
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testNullValueTransformation() throws Exception{
		String result = tobeTested.transform(null);
		
		Assert.assertNull(result);
	}
}
