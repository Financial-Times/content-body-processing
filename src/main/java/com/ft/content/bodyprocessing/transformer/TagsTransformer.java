package com.ft.content.bodyprocessing.transformer;

import org.apache.commons.lang.StringUtils;

/**
 * Removes XML and HTML tags from given field value
 * 
 * @author Ather Mughal
 */
public class TagsTransformer implements FieldTransformer {
	
	@Override
	public String transform(String fieldValue) throws TransformationException{
		if(StringUtils.isEmpty(fieldValue)){
			return null;
		}
		return fieldValue.replaceAll("\\<[^>]*>","");
	}
}
