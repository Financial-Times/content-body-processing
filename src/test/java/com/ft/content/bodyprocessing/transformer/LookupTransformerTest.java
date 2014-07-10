package com.ft.content.bodyprocessing.transformer;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class LookupTransformerTest {

	private LookupTransformer tobeTested;
	
	@Before
	public void setUp() throws Exception {
		tobeTested = new LookupTransformer();
		Map<String, String> lookup = new HashMap<String, String>();
		lookup.put("abC", "ZYz");
		
		tobeTested.setLookup(lookup);
		tobeTested.setDefaultValue("default");
	}
	
	@After
	public void tearDown() throws Exception{
		tobeTested = null;
	}
	
	@Test
	public void testSuccessfullLookupTransformation() throws Exception{
		String expected = "ZYz";
		String result = tobeTested.transform("abc");
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testUnSuccessfullLookupTransformation() throws Exception{
		String expected = "default";
		String result = tobeTested.transform("ab");
		Assert.assertEquals(expected, result);
		
	}
	
	@Test
	public void testNullValueTransformation() throws Exception{
		String result = tobeTested.transform(null);
		
		Assert.assertNull(result);
	}
}
