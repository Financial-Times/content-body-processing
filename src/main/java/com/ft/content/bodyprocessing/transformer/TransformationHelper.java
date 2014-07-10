package com.ft.content.bodyprocessing.transformer;

import java.util.List;

/**
 * Support class to facilities application of multiple transformation 
 * at a time on a given field value
 *  
 * @author Ather Mughal
 *
 */
public class TransformationHelper {
	
	public String applyTransformers(String fieldValue, List<FieldTransformer> transformers) throws TransformationException{
		
		//apply transformers one by one in the order provided by the caller
		for(FieldTransformer transformer : transformers){
			fieldValue = transformer.transform(fieldValue);
		}
		
		return fieldValue;
	}

}
