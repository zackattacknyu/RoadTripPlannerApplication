package com.zrd.rtp.model.client;

import com.zrd.rtp.model.data.StopSequenceSet;
import com.zrd.rtp.model.googleClient.DistanceMatrixApiRequest;
import com.zrd.rtp.model.googleData.DistanceMatrixData;

public class StopSequencesClient {

	public static StopSequenceSet getOrderedSequenceSet(String[] route) throws Exception{
		return StopSequenceSet.getOrderedSequences(getDistanceMatrixData(route));
	}
	
	public static StopSequenceSet getAllSequenceSet(String[] route) throws Exception{
		return StopSequenceSet.getAllSequences(getDistanceMatrixData(route));
	}
	
	public static DistanceMatrixData getDistanceMatrixData(String[] route){
		DistanceMatrixApiRequest request = DistanceMatrixApiRequest.makeRequest(route, route);
		return DistanceMatrixData.getDataFromJson(request.execute());
	}
}
