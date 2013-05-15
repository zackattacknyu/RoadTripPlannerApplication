package com.zrd.rtp.model.test;

import java.util.ArrayList;
import java.util.HashMap;

import com.zrd.rtp.model.data.Distance;
import com.zrd.rtp.model.data.StopSequence;
import com.zrd.rtp.model.data.StopSequenceSet;

public class SequenceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		StopSequenceSet mySet = StopSequenceSet.getAllSequences(4, 8);
		for(StopSequence stopSeq: mySet.getSequencesInBfsOrder()){
			mySet.addDistanceData(stopSeq,Distance.constructUsingMeters(1000 + Math.random()*10000000));
			System.out.println(stopSeq + ":" + stopSeq.getAddedDistance());
		}/*
		for(StopSequenceDistanceData distData: mySet.getSequencesInDistanceOrder()){
			System.out.println(distData.getAddedDistance().toString() + ":" + distData.getSequenceKey());
		}
		
		StopSequenceSet mySet2 = StopSequenceSet.getAllSequences(4, 8);
		for(StopSequence stopSeq: mySet.getSequencesInDfsOrder()){
			mySet.addTimeData(stopSeq,Duration.constructUsingSeconds(1000 + Math.random()*100000));
		}
		for(StopSequenceTimeData timeData: mySet.getSequencesInTimeOrder()){
			System.out.println(timeData.getAddedTime().toString() + ":" + timeData.getSequenceKey());
		}*/

	}

}
