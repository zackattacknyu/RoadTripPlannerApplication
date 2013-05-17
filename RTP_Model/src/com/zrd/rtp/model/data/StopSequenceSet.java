package com.zrd.rtp.model.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.zrd.rtp.model.exception.GoogleStatusCodeException;
import com.zrd.rtp.model.googleData.DistanceMatrixData;
import com.zrd.rtp.model.googleData.DistanceMatrixElement;


public class StopSequenceSet {

	private ArrayList<StopSequence> sequencesInDfsOrder;
	private DistanceMatrixData googleData;
	
	/*
	 * CONSTRUCTOR
	 */
	private StopSequenceSet(DistanceMatrixData googleData){
		sequencesInDfsOrder = new ArrayList<StopSequence>();
		this.googleData = googleData;
	}
	
	/*
	 * PUBLIC STATIC METHODS FOR CONSTRUCTION
	 */
	public static StopSequenceSet getAllSequences(DistanceMatrixData googleData, int offsetStopNumber) throws GoogleStatusCodeException{
		StopSequenceSet allSequences = new StopSequenceSet(googleData); 
		allSequences.getAllSequences(offsetStopNumber,googleData.getNumberStops());
		allSequences.constructSequenceData();
		return allSequences;
	}
	
	public static StopSequenceSet getAllSequences(DistanceMatrixData googleData) throws GoogleStatusCodeException{
		return getAllSequences(googleData,0);
	}
	
	public static StopSequenceSet getOrderedSequences(DistanceMatrixData googleData, int offsetStopNumber) throws GoogleStatusCodeException{
		StopSequenceSet orderedSequences = new StopSequenceSet(googleData);
		orderedSequences.getOrderedSequences(offsetStopNumber,googleData.getNumberStops());
		orderedSequences.constructSequenceData();
		return orderedSequences;
	}
	
	public static StopSequenceSet getOrderedSequences(DistanceMatrixData googleData) throws GoogleStatusCodeException{
		return getOrderedSequences(googleData,0);
	}
	
	/*
	 * PUBLIC METHODS FOR RETURNING DATA
	 */
	
	public StopSequence[] getSequencesInDfsOrder() {
		return sequencesInDfsOrder.toArray(new StopSequence[sequencesInDfsOrder.size()]);
	}

	public StopSequence[] getSequencesInBfsOrder(){
		StopSequence[] sequencesInBfsOrder = getSequencesInDfsOrder();
		Arrays.sort(sequencesInBfsOrder);
		return sequencesInBfsOrder;
	}
	
	/*
	 * PRIVATE HELPER METHODS WHEN CONSTRUCTING THE INITIAL SET
	 */
	private void getAllSequences(int offsetStopNumber, int numberStops){
		StopSequence startingSeq = new StopSequence(offsetStopNumber);
		startingSeq.addToSequence(0);
		generateAllSequences(startingSeq,1,numberStops+1);
	}

	private void getOrderedSequences(int offsetStopNumber, int numberStops){
		StopSequence startingSeq = new StopSequence(offsetStopNumber);
		startingSeq.addToSequence(0);
		generateOrderedSequences(startingSeq,1,numberStops+1);
	}
	
	private void generateAllSequences(StopSequence currentSequence, int starting, int ending){
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
	
	private void generateOrderedSequences(StopSequence currentSequence, int starting, int ending){
		StopSequence sequenceToAdd = currentSequence.clone();
		sequenceToAdd.addToSequence(ending);
		sequencesInDfsOrder.add(sequenceToAdd);
		for(int index = starting; index <= ending-1; index++){
			StopSequence nextSequence = currentSequence.clone();
			if(nextSequence.addToSequence(index)){
				generateOrderedSequences(nextSequence,index+1,ending);
			}
		}
	}
	
	/*
	 * PRIVATE HELPER METHOD FOR CONSTRUCTING THE IMPORTANT DATA
	 */
	
	private void constructSequenceData() throws GoogleStatusCodeException{
		if(!googleData.isGoodRequest()){
			throw new GoogleStatusCodeException("Illegal Status Code for processing"); //EXPAND UPON THIS
		}

		Duration baseDuration = Duration.constructUsingSeconds(googleData.getBaseElement().getDuration().getValue());
		
		Distance baseDistance = Distance.constructUsingMeters(googleData.getBaseElement().getDistance().getValue());
		
		for(StopSequence seq: sequencesInDfsOrder){
			calculateSequenceData(seq,baseDistance,baseDuration);
		}
		
	}
	
	private void calculateSequenceData(StopSequence seq, Distance baseDistance, Duration baseDuration) throws GoogleStatusCodeException{
		int stepOriginNumber;
		int stepDestNumber;
		DistanceMatrixElement currentElement;

		Duration currentSequenceDuration;
		Duration currentStepDuration;
		Distance currentSequenceDistance;
		Distance currentStepDistance;		
		
		currentSequenceDuration = Duration.constructUsingSeconds(0);
		currentSequenceDistance = Distance.constructUsingMeters(0);
		
		for(int index = 0; index < seq.getStopNumbers().size() - 1; index++){
			
			//get the beginning and end numbers and google element
			stepOriginNumber = seq.getStopNumbers().get(index);
			stepDestNumber = seq.getStopNumbers().get(index+1);
			currentElement = googleData.getElement(stepOriginNumber, stepDestNumber);
			
			//figure out the time/distance between the stops from the google data
			if(!currentElement.isValidRoute()){
				throw new GoogleStatusCodeException("Illegal Status Code for processing");
			}
			currentStepDuration = Duration.constructUsingSeconds(currentElement.getDuration().getValue());
			currentStepDistance = Distance.constructUsingMeters(currentElement.getDistance().getValue());
			
			//add that number to the current sequence duration/distance
			currentSequenceDuration = currentSequenceDuration.add(currentStepDuration);
			currentSequenceDistance = currentSequenceDistance.add(currentStepDistance);
		}
		
		//calculate the added time/distance over the base and put it in sequence data
		seq.setAddedTime(currentSequenceDuration.subtract(baseDuration));
		seq.setAddedDistance(currentSequenceDistance.subtract(baseDistance));
	}
}
