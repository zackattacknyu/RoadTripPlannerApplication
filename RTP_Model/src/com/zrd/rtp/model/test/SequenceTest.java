package com.zrd.rtp.model.test;

import java.util.ArrayList;
import java.util.HashMap;

import com.zrd.rtp.model.sequences.StopSequence;
import com.zrd.rtp.model.sequences.StopSequenceSet;

public class SequenceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		StopSequenceSet mySet = new StopSequenceSet();
		mySet.generateSequencesInOrder(4, 8);
		for(StopSequence stopSeq: mySet.getSequencesInDfsOrder()){
			System.out.println(stopSeq.toString());
		}

	}

}
