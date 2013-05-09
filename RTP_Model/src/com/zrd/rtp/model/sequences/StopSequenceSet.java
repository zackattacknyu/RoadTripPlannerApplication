package com.zrd.rtp.model.sequences;

import java.util.ArrayList;

public class StopSequenceSet {

	private ArrayList<StopSequence> sequencesInDfsOrder;
	
	public StopSequenceSet(){
		sequencesInDfsOrder = new ArrayList<StopSequence>();
	}
	
	public void generateSequences(int startingStopNumber, int endingStopNumber){
		StopSequence startingSeq = new StopSequence();
		startingSeq.addToSequence(startingStopNumber);
		generateSequences(startingSeq,startingStopNumber+1,endingStopNumber);
	}
	
	public ArrayList<StopSequence> getSequencesInDfsOrder() {
		return sequencesInDfsOrder;
	}

	public void generateSequences(StopSequence currentSequence, int starting, int ending){
		StopSequence sequenceToAdd = currentSequence.clone();
		sequenceToAdd.addToSequence(ending);
		sequencesInDfsOrder.add(sequenceToAdd);
		
		for(int index = starting; index <= ending-1; index++){
			StopSequence nextSequence = currentSequence.clone();
			if(nextSequence.addToSequence(index)){
				generateSequences(nextSequence,starting,ending);
			}
		}
	}
}
