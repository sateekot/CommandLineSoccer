package com.sateekot.soccer.exception;

import org.h2.api.ErrorCode;

/**
 * 
 * @author sateekot
 * Date 17-02-2019
 * Custom exception class for persistence layer related errors.
 *
 */
public class TeamAlreadyExistException extends Exception {

	private ErrorCode errorCode;
	
	public TeamAlreadyExistException(String message) {
		super(message);
	}
	
	public TeamAlreadyExistException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode=errorCode;
	}
	
	public TeamAlreadyExistException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public TeamAlreadyExistException(String message, ErrorCode errorCode, Throwable throwable) {
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
