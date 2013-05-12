package com.zrd.rtp.model.googleclient;

import java.util.HashMap;

public class DirectionsApiRequest extends BaseApiRequest {
	
	
	private DirectionsApiRequest(HashMap<String,String> queryParams) {
		super(ApiRequestConstants.GOOGLE_API_BASE_URL, 
				ApiRequestConstants.GOOGLE_DIRECTIONS_API_PATH_VALUES,queryParams);
	}
	
	public static DirectionsApiRequest makeRequest(String origin, String destination, String[] waypoints){
		HashMap<String,String> queryParams = new HashMap<String,String>();
		queryParams.put("origin", origin);
		queryParams.put("destination", destination);
		queryParams.put("waypoints", PipeSepartedValuesString.getString(waypoints));
		queryParams.put("sensor", "false");
		return new DirectionsApiRequest(queryParams);
	}

}
