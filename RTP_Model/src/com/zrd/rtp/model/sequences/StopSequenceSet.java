package com.zrd.rtp.model.sequences;

import java.util.ArrayList;

public class StopSequenceSet {

	private ArrayList<StopSequence> sequencesInDfsOrder;
	
	public StopSequenceSet(){
		sequencesInDfsOrder = new ArrayList<StopSequence>();
	}
	public ArrayList<StopSequence> getSequencesInDfsOrder() {
		return sequencesInDfsOrder;
	}
	
	public void generateAllSequences(int startingStopNumber, int endingStopNumber){
		StopSequence startingSeq = new StopSequence();
		startingSeq.addToSequence(startingStopNumber);
		generateAllSequences(startingSeq,startingStopNumber+1,endingStopNumber);
	}
	
	

	public void generateAllSequences(StopSequence currentSequence, int starting, int ending){
		StopSequence sequenceToAdd = currentSequence.clone();
		sequenceToAdd.addToSequence(ending);
		sequencesInDfsOrder.add(sequenceToAdd);
		
		for(int index = starting; index <= ending-1; index++){
			StopSequence nextSequence = currentSequence.clone();
			if(nextSequence.addToSequence(index)){
				generateAllSequences(nextSequence,starting,ending);
			}
		}
	}
	
	public void generateSequencesInOrder(int startingStopNumber, int endingStopNumber){
		StopSequence startingSeq = new StopSequence();
		startingSeq.addToSequence(startingStopNumber);
		generateSequencesInOrder(startingSeq,startingStopNumber+1,endingStopNumber);
	}
	
	

	public void generateSequencesInOrder(StopSequence currentSequence, int starting, int ending){
		StopSequence sequenceToAdd = currentSequence.clone();
		sequenceToAdd.addToSequence(ending);
		sequencesInDfsOrder.add(sequenceToAdd);
		for(int index = starting; index <= ending-1; index++){
			StopSequence nextSequence = currentSequence.clone();
			if(nextSequence.addToSequence(index)){
				generateSequencesInOrder(nextSequence,index+1,ending);
			}
		}
	}
}
