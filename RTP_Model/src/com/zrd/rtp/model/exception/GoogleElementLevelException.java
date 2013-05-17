package com.zrd.rtp.model.exception;

/**
 * This class if for element-level status code exceptions. 
 * The following URL has the information on the status codes:
 * 	https://developers.google.com/maps/documentation/distancematrix/#StatusCodes
 * 
 * @author Zach
 *
 */
public class GoogleElementLevelException extends Exception implements GoogleStatusCodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3742896100644983738L;
	private String statusCode;
	
	public GoogleElementLevelException(String statusCode) {
		super("Error computing individual route: " + statusCode);
		this.statusCode = statusCode;
	}

	@Override
	public String getStatusCodeName() {
		return statusCode;
	}

	@Override
	public String getStatusCodeDescription() {
		/*
		 * TODO: Convert the following data to descriptions: 
		 * OK indicates the response contains a valid result.
			NOT_FOUND indicates that the origin and/or destination 
				of this pairing could not be geocoded.
			ZERO_RESULTS indicates no route could 
				be found between the origin and destination.
		 */
		return null;
	}

}
