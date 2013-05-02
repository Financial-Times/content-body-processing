package com.ft.api.content.items.v1.services.bodyprocessing.writer;

import java.io.StringWriter;
import java.util.Map;

public class PlainTextBodyWriter implements BodyWriter {

	private StringWriter stringWriter = null;
	
	public PlainTextBodyWriter() {
		this.stringWriter = new StringWriter();
	}

	@Override
	public void flush() {
		// no need to flush a StringWriter
	}
	
	@Override
	public void close() {
		// no need to close a StringWriter
	}

	@Override
	public void write(String data) {
		stringWriter.write(data);
	}
	
	@Override
	public String asString() {
		return stringWriter.toString();
	}

	@Override
	public void writeStartTag(String name, Map<String, String> validAttributesAndValues) {
		throw new UnsupportedOperationException("PlainTextWriter should not write out start tags");
	}

	@Override
	public void writeEndTag(String name) {
		throw new UnsupportedOperationException("PlainTextWriter should not write out end tags");
	}

	@Override
	public void writeEntityReference(String name) {
		throw new UnsupportedOperationException("PlainTextWriter should not write out entity references");
	}

    @Override
    public boolean isPTagCurrentlyOpen() {
        throw new UnsupportedOperationException("PlainTextWriter should not need to know about <p> tags");
    }

}
