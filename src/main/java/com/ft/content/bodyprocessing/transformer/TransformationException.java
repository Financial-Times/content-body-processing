package com.ft.content.bodyprocessing.transformer;

/**
 * Exception representing tranformation exception 
 * 
 * @author Ather Mughal
 *
 */
public class TransformationException extends Exception {

	private static final long serialVersionUID = 1L;

	public TransformationException() {
		super();
	}

	public TransformationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TransformationException(String arg0) {
		super(arg0);
	}

	public TransformationException(Throwable arg0) {
		super(arg0);
	}
	
}
