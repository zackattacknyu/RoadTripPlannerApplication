package com.zrd.rtp.model.test;

import com.zrd.rtp.model.data.StopSequence;
import com.zrd.rtp.model.data.StopSequenceSet;
import com.zrd.rtp.model.exception.GoogleStatusCodeException;
import com.zrd.rtp.model.googleClient.DistanceMatrixApiRequest;
import com.zrd.rtp.model.googleData.DistanceMatrixData;

public class SequenceTest {

	/**
	 * @param args
	 * @throws GoogleStatusCodeException 
	 */
	public static void main(String[] args) throws Exception {
		String[] route = {"Davenport, IA","Dallas,TX","Santa Fe, NM","Tucson,AZ","Irvine, CA"};
		DistanceMatrixApiRequest request = DistanceMatrixApiRequest.makeRequest(route, route);
		DistanceMatrixData data = DistanceMatrixData.getDataFromJson(request.execute());
		printSequences(StopSequenceSet.getOrderedSequences(data));
		printSequences(StopSequenceSet.getAllSequences(data));
	}
	
	public static void printSequences(StopSequenceSet sequenceData){
		System.out.println();
		for(StopSequence seq:sequenceData.getSequencesInBfsOrder()){
			System.out.println(seq.toString()+ " : " + seq.getAddedDistance() + " : " + seq.getAddedTime());
		}
		System.out.println();
		for(StopSequence seq:sequenceData.getSequencesInDfsOrder()){
			System.out.println(seq.toString()+ " : " + seq.getAddedDistance() + " : " + seq.getAddedTime());
		}
	}

}
