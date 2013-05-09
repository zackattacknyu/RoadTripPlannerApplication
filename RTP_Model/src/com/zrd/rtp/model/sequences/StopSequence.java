package com.zrd.rtp.model.sequences;

import java.util.ArrayList;
import java.util.HashSet;

public class StopSequence implements Comparable<StopSequence>,Cloneable{

	private ArrayList<Integer> stopNumbers;
	private HashSet<Integer> stopNumbersSet;
	
	public StopSequence(){
		stopNumbers = new ArrayList<Integer>();
		stopNumbersSet = new HashSet<Integer>();
	}
	
	public boolean addToSequence(int stopNumber){
		if(stopNumbersSet.add(stopNumber)){
			stopNumbers.add(stopNumber);
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	public StopSequence clone(){
		StopSequence newSequence = new StopSequence();
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
			seqString.append(stopNumber);
			seqString.append("-");
		}
		
		//delete the last "-" appended
		seqString.deleteCharAt(seqString.length()-1);
		
		return seqString.toString();
	}

	public ArrayList<Integer> getStopNumbers() {
		return stopNumbers;
	}

	//PUT INTO BFS COMPARATOR LATER
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
