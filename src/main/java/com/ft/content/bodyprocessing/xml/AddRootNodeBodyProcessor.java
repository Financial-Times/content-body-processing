package com.ft.content.bodyprocessing.xml;

import static org.springframework.util.Assert.notNull;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.BodyProcessor;


public class AddRootNodeBodyProcessor implements BodyProcessor {
	
	private String rootNodeName;
	
	public AddRootNodeBodyProcessor(String rootNodeName){
		notNull(rootNodeName, "Root node name should not be null");
		this.rootNodeName = rootNodeName;
	}
	
	@Override
	public String process(String body, BodyProcessingContext bodyProcessingContext) {
		return getStartWrapperNode() + (body == null ? "" : body) + getEndWrapperNode();
	}
	
	private String getStartWrapperNode() {
		return "<" + rootNodeName + ">";
	}
	
	private String getEndWrapperNode() {
		return "</" + rootNodeName + ">";
	}

}
