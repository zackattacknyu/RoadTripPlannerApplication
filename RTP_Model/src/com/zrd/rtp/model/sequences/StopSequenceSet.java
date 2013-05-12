package com.zrd.rtp.model.sequences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.zrd.rtp.model.measurements.Distance;
import com.zrd.rtp.model.measurements.Duration;

public class StopSequenceSet {

	private ArrayList<StopSequence> sequencesInDfsOrder;
	private HashMap<String,StopSequence> sequencesMap;
	private ArrayList<StopSequenceTimeData> addedTimeData;
	private ArrayList<StopSequenceDistanceData> addedDistanceData;
	
	private StopSequenceSet(){
		sequencesInDfsOrder = new ArrayList<StopSequence>();
		sequencesMap = new HashMap<String,StopSequence>();
		
		addedTimeData = new ArrayList<StopSequenceTimeData>();
		addedDistanceData = new ArrayList<StopSequenceDistanceData>();
	}
	public StopSequence[] getSequencesInDfsOrder() {
		return sequencesInDfsOrder.toArray(new StopSequence[sequencesInDfsOrder.size()]);
	}
	
	private void addSequenceToSet(StopSequence seq){
		sequencesInDfsOrder.add(seq);
		sequencesMap.put(seq.toString(), seq);
	}
	
	public void addTimeData(StopSequence seq, Duration addedTime){
		addedTimeData.add(new StopSequenceTimeData(seq.toString(),addedTime));
	}
	
	public void addDistanceData(StopSequence seq, Distance addedDistance){
		addedDistanceData.add(new StopSequenceDistanceData(seq.toString(),addedDistance));
	}
	
	
	public StopSequence[] getSequencesInBfsOrder(){
		StopSequence[] sequencesInBfsOrder = getSequencesInDfsOrder();
		Arrays.sort(sequencesInBfsOrder);
		return sequencesInBfsOrder;
	}
	
	public StopSequenceTimeData[] getSequencesInTimeOrder(){
		StopSequenceTimeData[] sequencesInTimeOrder = 
				addedTimeData.toArray(new StopSequenceTimeData[addedTimeData.size()]);
		Arrays.sort(sequencesInTimeOrder);
		return sequencesInTimeOrder;
	}
	
	public StopSequenceDistanceData[] getSequencesInDistanceOrder(){
		StopSequenceDistanceData[] sequencesInDistanceOrder = 
				addedDistanceData.toArray(new StopSequenceDistanceData[addedDistanceData.size()]);
		Arrays.sort(sequencesInDistanceOrder);
		return sequencesInDistanceOrder;
	}
	
	public static StopSequenceSet getAllSequences(int startingStopNumber, int endingStopNumber){
		StopSequenceSet generatedSet = new StopSequenceSet();
		StopSequence startingSeq = new StopSequence();
		startingSeq.addToSequence(startingStopNumber);
		generatedSet.generateAllSequences(startingSeq,startingStopNumber+1,endingStopNumber);
		return generatedSet;
	}

	private void generateAllSequences(StopSequence currentSequence, int starting, int ending){
		StopSequence sequenceToAdd = currentSequence.clone();
		sequenceToAdd.addToSequence(ending);
		addSequenceToSet(sequenceToAdd);
		
		for(int index = starting; index <= ending-1; index++){
			StopSequence nextSequence = currentSequence.clone();
			if(nextSequence.addToSequence(index)){
				generateAllSequences(nextSequence,starting,ending);
			}
		}
	}
	
	
	public static StopSequenceSet getOrderedSequences(int startingStopNumber, int endingStopNumber){
		StopSequenceSet generatedSet = new StopSequenceSet();
		StopSequence startingSeq = new StopSequence();
		startingSeq.addToSequence(startingStopNumber);
		generatedSet.generateOrderedSequences(startingSeq,startingStopNumber+1,endingStopNumber);
		return generatedSet;
	}
	
	private void generateOrderedSequences(StopSequence currentSequence, int starting, int ending){
		StopSequence sequenceToAdd = currentSequence.clone();
		sequenceToAdd.addToSequence(ending);
		addSequenceToSet(sequenceToAdd);
		for(int index = starting; index <= ending-1; index++){
			StopSequence nextSequence = currentSequence.clone();
			if(nextSequence.addToSequence(index)){
				generateOrderedSequences(nextSequence,index+1,ending);
			}
		}
	}
}
