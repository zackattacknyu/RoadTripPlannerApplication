package com.zrd.rtp.model.googleclient;

import java.util.HashMap;

public class DistanceMatrixApiRequest extends BaseApiRequest {
	
	
	private DistanceMatrixApiRequest(HashMap<String,String> queryParams) {
		super(ApiRequestConstants.GOOGLE_API_BASE_URL, 
				ApiRequestConstants.GOOGLE_DISTANCE_MATRIX_API_PATH_VALUES,queryParams);
	}
	
	public static DistanceMatrixApiRequest makeRequest(String[] origins, String[] destinations){
		HashMap<String,String> queryParams = new HashMap<String,String>();
		queryParams.put("origins", PipeSepartedValuesString.getString(origins));
		queryParams.put("destinations", PipeSepartedValuesString.getString(destinations));
		queryParams.put("sensor", "false");
		return new DistanceMatrixApiRequest(queryParams);
	}

}
