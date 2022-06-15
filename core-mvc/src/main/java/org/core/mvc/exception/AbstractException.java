package org.core.mvc.exception;

import org.springframework.http.HttpStatus;

public class AbstractException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AbstractException(HttpStatus status, String specificMessage) {
		this.status = status;
		this.specificMessage = specificMessage;
	}
	
	private HttpStatus status;
	
	private String specificMessage;

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getSpecificMessage() {
		return specificMessage;
	}

	public void setSpecificMessage(String specificMessage) {
		this.specificMessage = specificMessage;
	}

}
