// =============================================================================
// $Id: cgo-codetemplates.xml,v 1.2 2005/09/02 07:01:26 rupert Exp $
// $Date: 2005/09/02 07:01:26 $
// $Revision: 34905 $
// $Author: rthomas $
// =============================================================================
package com.ft.content.bodyprocessing.transformer;

import de.ceyco.text.HtmlEntities2Latin1Charset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author Rupert Thomas
 * @author $Author: rthomas $
 * @version $Revision: 34905 $, $Date: 2005/09/02 07:01:26 $
 */
public class HTMLEntityTransformerTest
{
   @Test
   public void testHTMLEntityTransformer() throws Exception
   {
      HTMLEntityTransformer transformer = new HTMLEntityTransformer();
      String markedUp = "&quot;Hello&quot;, said Harry &amp; Hetty &lt;shhh..&gt;";
      String expected = "\"Hello\", said Harry & Hetty <shhh..>";
      String cleanedUp = transformer.transform(markedUp);
      
      assertEquals("Incorrect result", expected, cleanedUp);
   }
   
   @Test
   public void testHTMLEntityTransformerAgain() throws Exception
   {
      HTMLEntityTransformer transformer = new HTMLEntityTransformer();
      String markedUp = "&Ccedil;a va, Bj&ouml;rk?&nbsp;Ma&#241;ana it will cost &pound;5";
      
      // use the third party to assert the values rather than hard-coding the expected string and making it platform/editor dependant.
      String expected = HtmlEntities2Latin1Charset.convert(markedUp);
      String cleanedUp = transformer.transform(markedUp);
      assertEquals("Incorrect result", expected, cleanedUp);
   }   
}
