package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.content.items.v1.services.bodyprocessing.xml.StAXTransformingBodyProcessor;
import java.io.StringReader;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BackgroundNewsXMLParserTest{

	private XMLEventReader xmlEventReader;
	private BackgroundNewsXMLParser backgroundNewsXMLParser;
	@Mock private StAXTransformingBodyProcessor mockStAXTransformingBodyProcessor;
	@Mock private BodyProcessingContext mockBodyProcessingContext;
	private static final String EXPECTED_HEADER = "<p>BACKGROUND NEWS</p>";
	private static final String EXPECTED_TEXT = "<p>Although the UK has some well-known gay entrepreneurs, only a handful of openly gay people have run the largest companies.</p><p>These include Sir Charles Allen, former chief executive of ITV, executive chairman of Granada and chairman of EMI, currently regional ambassador for <a href=\"http://www.ft.com/intl/london-2012-olympics\">the Olympic Games</a>. Lord Browne was another, though he stayed in the closet for years.</p><p>There is also Michael Bishop, now Lord Glendonbrook, the ex-chairman of BMI, whose success in building up the airline led to a £223m sale to Lufthansa in 2009. He sits on the Conservative benches in the House of Lords and is a prominent voice for gay rights.</p><p>Another is Tim Hely Hutchison, group chief executive of Hachette Livre UK, Britain’s largest publisher, an old Etonian and son of an earl. He went to Oxford, but started his business career from scratch.</p><p>Entrepreneurs include Allegra McEvedy, the chef who co-founded Leon, the healthy fast-food chain. She gave up her role there in 2009 to focus on writing and television.</p><p>In the media, notable entrepreneurs include Eileen Gallagher, founder of Shed Media, creator of the television series <i>Footballers’ Wives</i>. She earned £3.8m from a £100m takeover by Time Warner.</p><p>Prominent gay figures in the City have included Ashley Steel, recently promoted to be vice-chairman of KPMG’s UK arm, and Robert Taylor, former chief executive of Kleinwort Benson.</p><p>Ernst &amp; Young, the professional services firm, recently headed the annual top 100 list of gay-friendly employers published by Stonewall, the gay rights organisation, followed by the Home Office and Barclays bank.</p><p>Others in the top 10 included investment bank Goldman Sachs, consultants Accenture and IBM, property group Gentoo and law firm Simmons &amp; Simmons.</p>";
	private String validXml ="<p><web-background-news channel=\"FTcom\"><table cellpadding=\"6px\" width=\"auto\"><tr><td><web-background-news-header><p>BACKGROUND NEWS</p></web-background-news-header></td></tr><tr><td><web-background-news-text><p>Although the UK has some well-known gay entrepreneurs, only a handful of openly gay people have run the largest companies.</p><p>These include Sir Charles Allen, former chief executive of ITV, executive chairman of Granada and chairman of EMI, currently regional ambassador for <a href=\"http://www.ft.com/intl/london-2012-olympics\">the Olympic Games</a>. Lord Browne was another, though he stayed in the closet for years.</p><p>There is also Michael Bishop, now Lord Glendonbrook, the ex-chairman of BMI, whose success in building up the airline led to a £223m sale to Lufthansa in 2009. He sits on the Conservative benches in the House of Lords and is a prominent voice for gay rights.</p><p>Another is Tim Hely Hutchison, group chief executive of Hachette Livre UK, Britain’s largest publisher, an old Etonian and son of an earl. He went to Oxford, but started his business career from scratch.</p><p>Entrepreneurs include Allegra McEvedy, the chef who co-founded Leon, the healthy fast-food chain. She gave up her role there in 2009 to focus on writing and television.</p><p>In the media, notable entrepreneurs include Eileen Gallagher, founder of Shed Media, creator of the television series <i>Footballers’ Wives</i>. She earned £3.8m from a £100m takeover by Time Warner.</p><p>Prominent gay figures in the City have included Ashley Steel, recently promoted to be vice-chairman of KPMG’s UK arm, and Robert Taylor, former chief executive of Kleinwort Benson.</p><p>Ernst &amp; Young, the professional services firm, recently headed the annual top 100 list of gay-friendly employers published by Stonewall, the gay rights organisation, followed by the Home Office and Barclays bank.</p><p>Others in the top 10 included investment bank Goldman Sachs, consultants Accenture and IBM, property group Gentoo and law firm Simmons &amp; Simmons.</p></web-background-news-text></td></tr></table></web-background-news></p>";
	private String xmlMissingHeaderElement ="<p><web-background-news channel=\"FTcom\"><table cellpadding=\"6px\" width=\"auto\"><tr><td></td></tr><tr><td><web-background-news-text><p>Although the UK has some well-known gay entrepreneurs, only a handful of openly gay people have run the largest companies.</p><p>These include Sir Charles Allen, former chief executive of ITV, executive chairman of Granada and chairman of EMI, currently regional ambassador for <a href=\"http://www.ft.com/intl/london-2012-olympics\">the Olympic Games</a>. Lord Browne was another, though he stayed in the closet for years.</p><p>There is also Michael Bishop, now Lord Glendonbrook, the ex-chairman of BMI, whose success in building up the airline led to a £223m sale to Lufthansa in 2009. He sits on the Conservative benches in the House of Lords and is a prominent voice for gay rights.</p><p>Another is Tim Hely Hutchison, group chief executive of Hachette Livre UK, Britain’s largest publisher, an old Etonian and son of an earl. He went to Oxford, but started his business career from scratch.</p><p>Entrepreneurs include Allegra McEvedy, the chef who co-founded Leon, the healthy fast-food chain. She gave up her role there in 2009 to focus on writing and television.</p><p>In the media, notable entrepreneurs include Eileen Gallagher, founder of Shed Media, creator of the television series <i>Footballers’ Wives</i>. She earned £3.8m from a £100m takeover by Time Warner.</p><p>Prominent gay figures in the City have included Ashley Steel, recently promoted to be vice-chairman of KPMG’s UK arm, and Robert Taylor, former chief executive of Kleinwort Benson.</p><p>Ernst &amp; Young, the professional services firm, recently headed the annual top 100 list of gay-friendly employers published by Stonewall, the gay rights organisation, followed by the Home Office and Barclays bank.</p><p>Others in the top 10 included investment bank Goldman Sachs, consultants Accenture and IBM, property group Gentoo and law firm Simmons &amp; Simmons.</p></web-background-news-text></td></tr></table></web-background-news></p>";
	private String xmlEmptyHeader ="<p><web-background-news channel=\"FTcom\"><table cellpadding=\"6px\" width=\"auto\"><tr><td><web-background-news-header></web-background-news-header></td></tr><tr><td><web-background-news-text><p>Although the UK has some well-known gay entrepreneurs, only a handful of openly gay people have run the largest companies.</p><p>These include Sir Charles Allen, former chief executive of ITV, executive chairman of Granada and chairman of EMI, currently regional ambassador for <a href=\"http://www.ft.com/intl/london-2012-olympics\">the Olympic Games</a>. Lord Browne was another, though he stayed in the closet for years.</p><p>There is also Michael Bishop, now Lord Glendonbrook, the ex-chairman of BMI, whose success in building up the airline led to a £223m sale to Lufthansa in 2009. He sits on the Conservative benches in the House of Lords and is a prominent voice for gay rights.</p><p>Another is Tim Hely Hutchison, group chief executive of Hachette Livre UK, Britain’s largest publisher, an old Etonian and son of an earl. He went to Oxford, but started his business career from scratch.</p><p>Entrepreneurs include Allegra McEvedy, the chef who co-founded Leon, the healthy fast-food chain. She gave up her role there in 2009 to focus on writing and television.</p><p>In the media, notable entrepreneurs include Eileen Gallagher, founder of Shed Media, creator of the television series <i>Footballers’ Wives</i>. She earned £3.8m from a £100m takeover by Time Warner.</p><p>Prominent gay figures in the City have included Ashley Steel, recently promoted to be vice-chairman of KPMG’s UK arm, and Robert Taylor, former chief executive of Kleinwort Benson.</p><p>Ernst &amp; Young, the professional services firm, recently headed the annual top 100 list of gay-friendly employers published by Stonewall, the gay rights organisation, followed by the Home Office and Barclays bank.</p><p>Others in the top 10 included investment bank Goldman Sachs, consultants Accenture and IBM, property group Gentoo and law firm Simmons &amp; Simmons.</p></web-background-news-text></td></tr></table></web-background-news></p>";
	private String xmlEmptyText ="<p><web-background-news channel=\"FTcom\"><table cellpadding=\"6px\" width=\"auto\"><tr><td><web-background-news-header><p>BACKGROUND NEWS</p></web-background-news-header></td></tr><tr><td><web-background-news-text></web-background-news-text></td></tr></table></web-background-news></p>";
	private String xmlMissingTextElement ="<p><web-background-news channel=\"FTcom\"><table cellpadding=\"6px\" width=\"auto\"><tr><td><web-background-news-header><p>BACKGROUND NEWS</p></web-background-news-header></td></tr></table></web-background-news></p>";
	private String xmlMissingTextAndHeaderElements ="<p><web-background-news channel=\"FTcom\"><table cellpadding=\"6px\" width=\"auto\"><tr><td></td></tr></table></web-background-news></p>";

	@Before
    public void setUp() {
		backgroundNewsXMLParser = new BackgroundNewsXMLParser(mockStAXTransformingBodyProcessor);
        when(mockStAXTransformingBodyProcessor.process(EXPECTED_HEADER, mockBodyProcessingContext)).thenReturn(EXPECTED_HEADER);
        when(mockStAXTransformingBodyProcessor.process(EXPECTED_TEXT, mockBodyProcessingContext)).thenReturn(EXPECTED_TEXT);
    }

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithStAXTransformingBodyNull() {
	  	new BackgroundNewsXMLParser(null);
	}

	@Test
	public void testParseChildElementData() throws XMLStreamException {
	  xmlEventReader = createReaderForXml(validXml);
	  BackgroundNewsData backgroundNewsData = backgroundNewsXMLParser.parseElementData(xmlEventReader);

	  assertNotNull("BackgroundNewsData should not be null", backgroundNewsData);
	  assertTrue(backgroundNewsData.isOkToRender());
	  assertEquals("Text was not as expected", EXPECTED_HEADER, backgroundNewsData.getHeader().trim());
	  assertEquals("Source was not as expected", EXPECTED_TEXT, backgroundNewsData.getText().trim());
	}

	@Test
   public void testParseChildElementDataWithMissingTextElement() throws XMLStreamException {
       xmlEventReader = createReaderForXml(xmlMissingTextElement);
	 	BackgroundNewsData backgroundNewsData = backgroundNewsXMLParser.parseElementData(xmlEventReader);
       assertNotNull("PullQuoteData should not be null", backgroundNewsData);
       assertTrue(backgroundNewsData.isOkToRender());
       assertNull("Text was not as expected", backgroundNewsData.getText());
       assertEquals("Source was not as expected", EXPECTED_HEADER, backgroundNewsData.getHeader());
   }

   @Test
   public void testParseChildElementDataWithMissingHeaderElement() throws XMLStreamException {
       xmlEventReader = createReaderForXml(xmlMissingHeaderElement);
	   BackgroundNewsData backgroundNewsData  = backgroundNewsXMLParser.parseElementData(xmlEventReader);
       assertNotNull("PullQuoteData should not be null", backgroundNewsData);
       assertTrue(backgroundNewsData.isOkToRender());
       assertEquals("Text was not as expected",EXPECTED_TEXT, backgroundNewsData.getText());
       assertNull("Source was not as expected", backgroundNewsData.getHeader());
   }


	@Test
	public void testParseChildElementDataWithMissingTextAndHeaderElements() throws XMLStreamException {
		xmlEventReader = createReaderForXml(xmlMissingTextAndHeaderElements);
		BackgroundNewsData backgroundNewsData  = backgroundNewsXMLParser.parseElementData(xmlEventReader);
		assertNotNull(backgroundNewsData);
		assertFalse(backgroundNewsData.isOkToRender());
	}

	@Test
	public void testParseChildElementDataWithEmptyTextData() throws XMLStreamException {
		xmlEventReader = createReaderForXml(xmlEmptyText);
		BackgroundNewsData backgroundNewsData  = backgroundNewsXMLParser.parseElementData(xmlEventReader);
		assertNotNull("PullQuoteData should not be null", backgroundNewsData);
		assertTrue(backgroundNewsData.isOkToRender());
		assertEquals("Text was not as expected", "", backgroundNewsData.getText());
		assertEquals("Source was not as expected", EXPECTED_HEADER, backgroundNewsData.getHeader());
	}

	@Test
	public void testParseChildElementDataWithEmptyHeaderData() throws XMLStreamException {
		xmlEventReader = createReaderForXml(xmlEmptyHeader);
		BackgroundNewsData backgroundNewsData  = backgroundNewsXMLParser.parseElementData(xmlEventReader);
		assertNotNull("PullQuoteData should not be null", backgroundNewsData);
		assertTrue(backgroundNewsData.isOkToRender());
		assertEquals("Text was not as expected",EXPECTED_TEXT, backgroundNewsData.getText());
		assertEquals("Source was not as expected", "", backgroundNewsData.getHeader());
	}

	private XMLEventReader createReaderForXml(String xml) throws XMLStreamException {
		 XMLInputFactory newInstance = XMLInputFactory2.newInstance();
		 StringReader reader = new StringReader(xml);
		 return newInstance.createXMLEventReader(reader);
	}
}
