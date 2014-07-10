package com.ft.content.bodyprocessing.transformer;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DuplicateWhiteSpaceTransformerTest {

	private DuplicateWhiteSpaceTransformer tobeTested;
	
	@Before
	public void setUp() throws Exception {
		tobeTested = new DuplicateWhiteSpaceTransformer();
	}
	
	@After
	public void tearDown() throws Exception{
		tobeTested = null;
	}
	
	@Test
	public void testDuplicateSpaceRemoval() throws Exception{
		String actual = "This   is A testing   STriNg";
		String expected = "This is A testing STriNg";
		
		String result = tobeTested.transform(actual);
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testNoSpaceRemoval() throws Exception{
		String actual = "This is A testing STriNg";
		String expected = "This is A testing STriNg";
		
		String result = tobeTested.transform(actual);
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testNullValueTransformation() throws Exception{
		String result = tobeTested.transform(null);
		
		Assert.assertNull(result);
	}
	
	
}
