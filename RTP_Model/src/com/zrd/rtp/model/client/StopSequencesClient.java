package com.zrd.rtp.model.client;

import com.zrd.rtp.model.data.StopSequenceSet;
import com.zrd.rtp.model.googleClient.DistanceMatrixApiRequest;
import com.zrd.rtp.model.googleData.DistanceMatrixData;

public class StopSequencesClient {

	public static StopSequenceSet getSequenceSet(String[] route) throws Exception{
		DistanceMatrixApiRequest request = DistanceMatrixApiRequest.makeRequest(route, route);
		DistanceMatrixData data = DistanceMatrixData.getDataFromJson(request.execute());
		return StopSequenceSet.getOrderedSequences(data);
	}
}
