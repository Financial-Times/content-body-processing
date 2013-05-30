package com.ft.content.bodyprocessing.writer;


public class PlainTextBodyWriterFactory implements BodyWriterFactory {
	
	@Override
	public BodyWriter createBodyWriter() {
		return new PlainTextBodyWriter();
	}

}
