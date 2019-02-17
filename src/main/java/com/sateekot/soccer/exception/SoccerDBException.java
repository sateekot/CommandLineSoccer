package com.sateekot.soccer.exception;

import org.h2.api.ErrorCode;

/**
 * 
 * @author sateekot
 * Date 15-02-2019
 * Custom exception class for persistence layer related errors.
 *
 */
public class SoccerDBException extends Exception {

	private ErrorCode errorCode;
	
	public SoccerDBException(String message) {
		super(message);
	}
	
	public SoccerDBException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode=errorCode;
	}
	
	public SoccerDBException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public SoccerDBException(String message, ErrorCode errorCode, Throwable throwable) {
		super(message,throwable);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
		
}
