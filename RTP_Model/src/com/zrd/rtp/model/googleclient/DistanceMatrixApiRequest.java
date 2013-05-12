package com.zrd.rtp.model.googleclient;

import java.util.HashMap;

public class DistanceMatrixApiRequest extends BaseApiRequest {
	
	
	public DistanceMatrixApiRequest(String[] origins, String[] destinations) {
		super(ApiRequestConstants.GOOGLE_API_BASE_URL, 
				ApiRequestConstants.GOOGLE_DIRECTIONS_API_PATH_VALUES);
		
		String originsString = PipeSepartedValuesString.getString(origins);
		String destinationsString = PipeSepartedValuesString.getString(destinations);
		HashMap<String,String> queryParams = new HashMap<String,String>();
		queryParams.put("origins", originsString);
		queryParams.put("destinations", destinationsString);
		queryParams.put("sensor", "false");
		setQueryParameters(queryParams);
		
		initializeQueryParameters();
		initializeResourceBuilder();
	}

}
