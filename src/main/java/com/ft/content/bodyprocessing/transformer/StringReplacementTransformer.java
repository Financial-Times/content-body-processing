package com.ft.content.bodyprocessing.transformer;

import org.apache.commons.lang.StringUtils;

/**
 * Transform string/character into another string/character within given field value
 * 
 * @author Ather Mughal
 */
public class StringReplacementTransformer extends PatternBasedTransformer {
	
	//-- Data members
	private String to; 
	
	//-- Constructor
	public StringReplacementTransformer(String fromPattern) {
		super(fromPattern);
	}
	
	@Override
	public String transform(String fieldValue) throws TransformationException{
		if(StringUtils.isEmpty(fieldValue)){
			return null;
		}
		
		return getMatcher(fieldValue).replaceAll(to);
	}
	
	//-- Getters & Setters
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
