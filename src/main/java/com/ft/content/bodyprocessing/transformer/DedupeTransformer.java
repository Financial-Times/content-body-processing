package com.ft.content.bodyprocessing.transformer;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Removes duplicate values from delimited field value
 * 
 * @author Ather Mughal
 *
 */
public class DedupeTransformer implements FieldTransformer {

	public static final String DEFAULT_DELIMITER = ";";
	
	private String delimiter = DEFAULT_DELIMITER;
	
	@Override
	public String transform(String fieldValue) throws TransformationException {
		if(StringUtils.isEmpty(fieldValue)){
			return null;
		}
		
		List<String> result = new ArrayList<String>();
		
		//tokenize string
		StringTokenizer st = new StringTokenizer(fieldValue, delimiter);
		while(st.hasMoreTokens()){
			String token = st.nextToken();
			if( (StringUtils.isNotBlank(token)) && (!result.contains(token)) ){
				result.add(token);
			}
		}
		
		//concat strings
		StringBuilder sb = new StringBuilder();
		for(String value : result){
			sb.append(delimiter);
			sb.append(value);
		}
		
		return sb.toString().substring(1);
	}

	//-- Getters & Setters
	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

}
