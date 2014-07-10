package com.ft.content.bodyprocessing.transformer;

public interface FieldTransformer {
	
	/**
	 * Transform given src string and return modified version of it
	 * 
	 * @param fieldValue Field to be transformed
	 * @return transformed string
	 */
	public String transform(String fieldValue) throws TransformationException;

}
