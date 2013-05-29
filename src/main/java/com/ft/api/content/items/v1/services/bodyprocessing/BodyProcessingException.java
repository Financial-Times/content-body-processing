package com.ft.api.content.items.v1.services.bodyprocessing;

public class BodyProcessingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BodyProcessingException(Throwable throwable) {
		super(throwable);
	}
	
	public BodyProcessingException(String message) {
		super(message);
	}
}
