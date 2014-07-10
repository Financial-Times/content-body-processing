// =============================================================================
// $Id: cgo-codetemplates.xml,v 1.2 2005/09/02 07:01:26 rupert Exp $
// $Date: 2005/09/02 07:01:26 $
// $Revision: 34905 $
// $Author: rthomas $
// =============================================================================
package com.ft.content.bodyprocessing.transformer;

import de.ceyco.text.HtmlEntities2Latin1Charset;

/**
 * @author Rupert Thomas
 * @author $Author: rthomas $
 * @version $Revision: 34905 $, $Date: 2005/09/02 07:01:26 $
 */
public class HTMLEntityTransformer implements FieldTransformer
{
   /**
    * @see com.ft.publish.transformer.FieldTransformer#transform(String)
    */
   @Override
   public String transform(String fieldValue) throws TransformationException
   {
      return HtmlEntities2Latin1Charset.convert(fieldValue);
   }

}
