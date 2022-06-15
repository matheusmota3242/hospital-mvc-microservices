package org.management.service;

import org.core.mvc.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class PatientNotFoundException extends AbstractException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PatientNotFoundException(HttpStatus status, String specificMessage) {
		super(status, specificMessage);
	}
}
