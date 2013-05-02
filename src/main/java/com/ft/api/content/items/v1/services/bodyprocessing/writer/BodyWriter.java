package com.ft.api.content.items.v1.services.bodyprocessing.writer;

import java.util.Map;


public interface BodyWriter {

	void close();

	void write(String data);

	void writeStartTag(String name, Map<String, String> validAttributesAndValues);

	void writeEndTag(String name);
	
	void writeEntityReference(String name);

	String asString();

	void flush();
	
    boolean isPTagCurrentlyOpen();
}
