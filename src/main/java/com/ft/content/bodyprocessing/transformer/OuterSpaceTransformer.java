package com.ft.content.bodyprocessing.transformer;

import org.apache.commons.lang.StringUtils;


/**
 * Strips out the leading and trailing spaces from field value
 * 
 * @author Ather Mughal
 *
 */
public class OuterSpaceTransformer implements FieldTransformer{

	@Override
	public String transform(String fieldValue) throws TransformationException{
		if(StringUtils.isEmpty(fieldValue)){
			return null;
		}
		
		return fieldValue.trim();
	}

}
