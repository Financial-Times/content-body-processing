// =============================================================================
// $Id: ErrorConstants.java 9522 2009-04-06 16:20:38Z rthomas $
// $Date: 2009-04-06 17:20:38 +0100 (Mon, 06 Apr 2009) $
// $Revision: 9522 $
// $Author: rthomas $
// =============================================================================
package com.ft.content.bodyprocessing.transformer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PatternBasedTransformer implements FieldTransformer {

	private Pattern pattern;
	
	protected PatternBasedTransformer(String pattern)
	{
		this.pattern = Pattern.compile(pattern,Pattern.DOTALL);
	}
		
	protected Matcher getMatcher(String fieldValue) {
		Matcher matcher = pattern.matcher(fieldValue);
		return matcher;
	}

}
