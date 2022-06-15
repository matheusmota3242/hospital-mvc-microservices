package org.core.mvc.exception;

import org.springframework.http.HttpStatus;

public class InternmentExcepion extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InternmentExcepion(HttpStatus status, String specificMessage) {
		super(status, specificMessage);
	}

}
