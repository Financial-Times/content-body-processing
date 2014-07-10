package com.ft.content.bodyprocessing.transformer;

import org.apache.commons.lang.StringUtils;

/**
 * Removes duplicate white spaces from given field value
 * 
 * @author Ather Mughal
 * 
 */
public class DuplicateWhiteSpaceTransformer extends PatternBasedTransformer {

	private static final String DOUBLE_WHITESPACE_REGEX = "\\s+";

	public DuplicateWhiteSpaceTransformer() {
		super(DOUBLE_WHITESPACE_REGEX);
	}

	@Override
	public String transform(String fieldValue) throws TransformationException {
		if (StringUtils.isEmpty(fieldValue)) {
			return null;
		}

		return getMatcher(fieldValue).replaceAll(" ");
	}

}
