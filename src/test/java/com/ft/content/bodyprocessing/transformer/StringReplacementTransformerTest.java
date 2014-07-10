package com.ft.content.bodyprocessing.transformer;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StringReplacementTransformerTest{

	private StringReplacementTransformer tobeTested;
	
	@Before
	public void setUp() throws Exception {
		tobeTested = new StringReplacementTransformer("_");
		tobeTested.setTo(" ");
	}
	
	@After
	public void tearDown() throws Exception{
		tobeTested = null;
	}
	
	@Test
	public void testUnderscoreTranformationToSpaceChar() throws Exception{
		String expected = "test of tag removal";
		String str = "test_of_tag_removal";
		
		String result = tobeTested.transform(str);
		
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testNullValueTransformation() throws Exception{
		String result = tobeTested.transform(null);
		
		Assert.assertNull(result);
	}
	
    @Test
    public void testPullTagTransformation() throws Exception{
       String str = "Hello <pull-quote align=\"right\" type=\"special\">&lt;Hahahahah</pull-quote>World!";
       String expected= "Hello World!";
       String pattern = "<pull-quote\\s.*</pull-quote>";
       
       StringReplacementTransformer pullTagTransformer = new StringReplacementTransformer(pattern);
       pullTagTransformer.setTo("");
       String result = pullTagTransformer.transform(str);
       
       Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testRegexTransformation() throws Exception{
    	String str = "embed1^M ^M Chinese stocks defied losses elsewhere in ASIA on Thursday, amid speculation that falls earlier in the week in response to the country's interest rate hike w ere excessive";

    	String pattern = "embed\\d+";
    	StringReplacementTransformer regexTransformer = new StringReplacementTransformer(pattern);
        regexTransformer.setTo("");
        String result = regexTransformer.transform(str);
        Assert.assertTrue(result.indexOf("embed1")== -1);
    }
    
    @Test
    public void testRegexSquareBraketTransformation() throws Exception{
    	String str = "teaser contains tags [caption id=\"attachment_50041\" align=\"alignleft\" width=\"167\" captioncaption] test [caption id=\"attachment_50041\" align=\"alignleft\" width=\"167\" captioncaption] test";
    	String expected="teaser contains tags  test  test";
    	String pattern = "\\[\\/?caption[^]]*\\]";
    	StringReplacementTransformer regexTransformer = new StringReplacementTransformer(pattern);
        regexTransformer.setTo("");
        String result = regexTransformer.transform(str);
        Assert.assertEquals(expected, result);
    }
}
