package com.ft.api.content.items.v1.services.bodyprocessing;

import com.ft.api.utils.exception.UnexpectedException;

public class BodyProcessingException extends UnexpectedException {

	private static final long serialVersionUID = 1L;

	public BodyProcessingException(Throwable throwable) {
		super(throwable);
	}
	
	public BodyProcessingException(String message) {
		super(message);
	}
}
