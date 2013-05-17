package com.zrd.rtp.model.exception;

public class GoogleStatusCodeException extends Exception {
	
	private String statusCodeName;
	private String statusCodeDescription;

	public GoogleStatusCodeException(String statusCode) {
		super(String.format("Error: Google returned the following status code:%s", statusCode));
		statusCodeName = statusCode;
	}
}
