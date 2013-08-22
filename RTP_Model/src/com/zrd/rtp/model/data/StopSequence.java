package com.zrd.rtp.model.data;

import java.util.ArrayList;
import java.util.HashSet;


public class StopSequence implements Comparable<StopSequence>,Cloneable{

	//this adds the stop and ensures the order
	private ArrayList<Integer> stopNumbers;
	
	//this is used to add the stop and ensure uniqueness
	private HashSet<Integer> stopNumbersSet;
	
	private Distance addedDistance;
	private Duration addedTime;
	private int stopsHashNumber = 0;
	
	/**
	 * This stops hash number is an integer that represents the stops 
	 * 		contained in the sequence. If two sequences have the same hash number,
	 * 		then the stops contained in the sequences are the same. The order
	 * 		of the stops though could vary. 
	 * @return
	 */
	public int getStopsHashNumber() {
		return stopsHashNumber;
	}

	private int offsetStopNumber;
	
	public Distance getAddedDistance() {
		return addedDistance;
	}
	
	public boolean hasStop(int stopNum){
		return stopNumbersSet.contains(stopNum);
	}

	public void setAddedDistance(Distance addedDistance) {
		this.addedDistance = addedDistance;
	}

	public Duration getAddedTime() {
		return addedTime;
	}

	public void setAddedTime(Duration addedDuration) {
		this.addedTime = addedDuration;
	}

	public StopSequence(int offsetStopNumber){
		this.offsetStopNumber = offsetStopNumber;
		stopNumbers = new ArrayList<Integer>();
		stopNumbersSet = new HashSet<Integer>();
	}
	
	public boolean addToSequence(int stopNumber){
		if(stopNumbersSet.add(stopNumber)){
			stopNumbers.add(stopNumber);
			stopsHashNumber += Math.pow(2, stopNumber);
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	public StopSequence clone(){
		StopSequence newSequence = new StopSequence(offsetStopNumber);
		for(int stopNumber: stopNumbers){
			newSequence.addToSequence(stopNumber);
		}
		return newSequence;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StopSequence){
			StopSequence other = (StopSequence)obj;
			return other.toString().equals(this.toString());
		}
		else{
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuilder seqString = new StringBuilder();
		for(int stopNumber:stopNumbers){
			seqString.append(stopNumber + offsetStopNumber);
			seqString.append("-");
		}
		
		//delete the last "-" appended
		seqString.deleteCharAt(seqString.length()-1);
		
		return seqString.toString();
	}

	public ArrayList<Integer> getStopNumbers() {
		return stopNumbers;
	}

	//This is the BFS Comparator
	@Override
	public int compareTo(StopSequence arg0) {
		if(stopNumbers.size() < arg0.getStopNumbers().size()){
			return -1;
		}else if(stopNumbers.size() > arg0.getStopNumbers().size()){
			return 1;
		}else{
			
			for(int index = 0; index < stopNumbers.size(); index++){
				if(stopNumbers.get(index) < arg0.getStopNumbers().get(index)){
					return -1;
				}else if(stopNumbers.get(index) > arg0.getStopNumbers().get(index)){
					return 1;
				}
			}
			
			return 0;
			
		}
	}
}
