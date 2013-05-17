package com.zrd.rtp.model.exception;

/**
 * This class if for top-level status code exceptions. 
 * The following URL has the information on the status codes:
 * 	https://developers.google.com/maps/documentation/distancematrix/#StatusCodes
 * 
 * @author Zach
 *
 */
public class GoogleTopLevelException extends Exception implements GoogleStatusCodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String statusCode;
	
	public GoogleTopLevelException(String statusCode) {
		super("Error computing distance matrix data: " + statusCode);
		this.statusCode = statusCode;
	}

	@Override
	public String getStatusCodeName() {
		return statusCode;
	}

	@Override
	public String getStatusCodeDescription() {
		/*
		 *TODO: Add the following data as descriptions
		 *	OK indicates the response contains a valid result.
			INVALID_REQUEST indicates that the provided request was invalid.
			MAX_ELEMENTS_EXCEEDED indicates that the product of 
				origins and destinations exceeds the per-query limit.
			OVER_QUERY_LIMIT indicates the service has received 
				too many requests from your application within the allowed time period.
			REQUEST_DENIED indicates that the service denied use of the 
				Distance Matrix service by your application.
			UNKNOWN_ERROR indicates a Distance Matrix request 
				could not be processed due to a server error. 
				The request may succeed if you try again. 
		 */
		return null;
	}

	

}
