package com.ft.api.content.items.v1.services.bodyprocessing;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.ft.api.content.items.v1.services.bodyprocessing.regex.RegexRemoverBodyProcessor;

public class RegexRemoverBodyProcessorTest {

	private RegexRemoverBodyProcessor bodyProcessor = null;
	
	@Before
	public void setup() {
		bodyProcessor = new RegexRemoverBodyProcessor("\n+");
	}
	
	@Test
	public void singleNewLineIsRemoved() {
		String actualTransformedBody = bodyProcessor.process("\n", null);
		assertEquals("", actualTransformedBody);
	}
	
	@Test
	public void twoNewLinesTogetherAreRemoved() {
		String actualTransformedBody = bodyProcessor.process("\n\n", null);
		assertEquals("", actualTransformedBody);
	}
	
	@Test
	public void threeNewLinesTogetherAreRemoved() {
		String actualTransformedBody = bodyProcessor.process("\n\n\n", null);
		assertEquals("", actualTransformedBody);
	}

	@Test
	public void newLinesInterspersedInTextAreRemoved() {
		String actualTransformedBody = bodyProcessor.process("Text \n Text \n\n Text", null);
		assertEquals("Text  Text  Text", actualTransformedBody);
	}

}
