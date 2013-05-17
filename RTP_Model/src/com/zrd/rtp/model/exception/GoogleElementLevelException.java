package com.zrd.rtp.model.exception;

/**
 * This class if for element-level status code exceptions. 
 * The following URL has the information on the status codes:
 * 	https://developers.google.com/maps/documentation/distancematrix/#StatusCodes
 * 
 * @author Zach
 *
 */
public class GoogleElementLevelException extends GoogleStatusCodeException {

	public GoogleElementLevelException(String statusCode) {
		super(statusCode);
	}

}
