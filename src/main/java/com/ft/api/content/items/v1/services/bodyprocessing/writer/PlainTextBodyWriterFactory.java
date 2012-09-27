package com.ft.api.content.items.v1.services.bodyprocessing.writer;


public class PlainTextBodyWriterFactory implements BodyWriterFactory {
	
	@Override
	public BodyWriter createBodyWriter() {
		return new PlainTextBodyWriter();
	}

}
