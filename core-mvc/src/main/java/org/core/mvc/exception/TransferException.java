package org.core.mvc.exception;

import org.springframework.http.HttpStatus;

public class TransferException extends AbstractException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TransferException(HttpStatus status, String specificMessage) {
		super(status, specificMessage);
	}

}
