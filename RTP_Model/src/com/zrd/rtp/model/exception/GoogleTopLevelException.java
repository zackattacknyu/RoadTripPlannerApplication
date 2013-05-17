package com.zrd.rtp.model.exception;

/**
 * This class if for top-level status code exceptions. 
 * The following URL has the information on the status codes:
 * 	https://developers.google.com/maps/documentation/distancematrix/#StatusCodes
 * 
 * @author Zach
 *
 */
public class GoogleTopLevelException extends GoogleStatusCodeException {

	public GoogleTopLevelException(String statusCode) {
		super(statusCode);
		// TODO Auto-generated constructor stub
	}

}
