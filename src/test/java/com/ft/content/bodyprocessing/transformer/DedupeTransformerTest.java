package com.ft.content.bodyprocessing.transformer;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DedupeTransformerTest {

	private DedupeTransformer tobeTested;
	
	@Before
	public void setUp() throws Exception {
		tobeTested = new DedupeTransformer();
		tobeTested.setDelimiter(";");
	}
	
	@After
	public void tearDown() throws Exception{
		tobeTested = null;
	}
	
	@Test
	public void testMultipleDuplicateValuesRemoval() throws Exception{
		String expected = "abc;def;ghi;jkl;mno";
		String str = "abc;def;abc;ghi;def;jkl;mno;jkl";
		
		Assert.assertEquals(expected, tobeTested.transform(str));
		
	}
	
	@Test
	public void testCaseSensitiveMultipleDuplicateValuesRemoval() throws Exception{
		String expected = "abc;def;ABC;ghi;Def;jkl;mno";
		String str = "abc;def;ABC;ghi;ABC;Def;jkl;Def;mno;jkl";
		
		Assert.assertEquals(expected, tobeTested.transform(str));
		
	}
	
	@Test
	public void testNoDuplicateValuesRemoval() throws Exception{
		String expected = "abc";
		String str = "abc";
		
		Assert.assertEquals(expected, tobeTested.transform(str));
	}
	
	@Test
	public void testNullAndEmptyValueTransformation() throws Exception{
		Assert.assertNull(tobeTested.transform(null));
		Assert.assertNull(tobeTested.transform(""));
	}
	
}
