package com.ft.content.bodyprocessing.transformer;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Transformer that transform field using lookup (i.e. key/value map). This tranformation is case 
 * insensitive. For example, if lookup key/value is "ITIF/New Value", then the fieldValue of 
 * "itif" will be transformed to "New Value" 
 * 
 * @author Ather Mughal
 *
 */
public class LookupTransformer implements FieldTransformer {

	private Map<String, String> lookup = new HashMap<String, String>();
	private String defaultValue;
	
	@Override
	public String transform(String fieldValue) throws TransformationException{
		if(StringUtils.isEmpty(fieldValue)){
			return null;
		}
		
		if(lookup.containsKey(fieldValue.toLowerCase())){
			return lookup.get(fieldValue.toLowerCase());
		} else {
			return defaultValue;
		}
		
	}

	//-- Getters & Setters
	public Map<String, String> getLookup() {
		return lookup;
	}

	public void setLookup(Map<String, String> lookup) {
		if(lookup != null){
			this.lookup = new HashMap<String, String>(lookup.size());
			for(String key: lookup.keySet()){
				this.lookup.put(key.toLowerCase(), lookup.get(key));
			}
		}
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
