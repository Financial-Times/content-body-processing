package com.ft.content.bodyprocessing.regex;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.BodyProcessor;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexReplacerBodyProcessor implements BodyProcessor {

	private Map<Pattern,String> patternsMap = new HashMap<Pattern,String>();

	public RegexReplacerBodyProcessor(String stringPattern, String replacementString) {
		addPattern(stringPattern, replacementString);
	}

	private void addPattern(String stringPattern, String replacementString) {
		Pattern pattern = Pattern.compile(stringPattern, Pattern.DOTALL);
        this.patternsMap.put(pattern, replacementString);
	}

	@Override
	public String process(String body, BodyProcessingContext bodyProcessingContext) {
		String processedBody = body;
        for (Pattern pattern : patternsMap.keySet()) {
			Matcher matcher = pattern.matcher(processedBody);
	        processedBody = matcher.replaceAll(patternsMap.get(pattern));
		}
		return processedBody;
	}

}
