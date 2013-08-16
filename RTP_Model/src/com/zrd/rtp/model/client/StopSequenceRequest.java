package com.zrd.rtp.model.client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.zrd.rtp.model.data.StopSequence;
import com.zrd.rtp.model.data.StopSequenceSet;
import com.zrd.rtp.model.googleClient.DistanceMatrixApiRequest;
import com.zrd.rtp.model.googleData.DistanceMatrixData;

public class StopSequenceRequest {
	
	public enum OrderingOption{BFS_ORDER, DFS_ORDER,BEST_SEQUENCES_BY_TIME,BEST_SEQUENCES_BY_DISTANCE};
	
	public enum SequencesOption{ALL_SEQUENCES,ONLY_ORDERED_SEQUENCES,BEST_SEQUENCES};
	
	public enum MeasurementUnitOption{METRIC,IMPERIAL};
	
	private List<Object[]> sequenceDataTable;
	private String[] outputAddresses;
	private String[] inputRoute;
	private StopSequenceSet seqSet;
	private StopSequence[] sequenceArray;
	private OrderingOption ordering;
	private SequencesOption sequenceOption;
	private MeasurementUnitOption measurementUnit;
	
	private StopSequenceRequest(String[] route, OrderingOption ordering, 
			SequencesOption sequenceOption, MeasurementUnitOption measurementUnit) throws Exception{
		this.inputRoute = route;
		this.ordering = ordering;
		this.sequenceOption = sequenceOption;
		this.measurementUnit = measurementUnit;
		
		makeSequenceSet();
		makeSequenceArray();
		makeOutputAddressArray();
		makeSequenceDataTable();
		
	}
	public List<Object[]> getSequenceDataTable() {
		return sequenceDataTable;
	}
	public List<Object[]> getOutputAddresses() {
		List<Object[]> outputList = new ArrayList<Object[]>(outputAddresses.length);
		Object[] currentRow;
		for(int index = 0; index < outputAddresses.length; index++){
			currentRow = new Object[2];
			currentRow[0] = Double.valueOf(index);
			currentRow[1] = outputAddresses[index];
			outputList.add(currentRow);
		}
		return outputList;
	}
	public static StopSequenceRequest getSequenceData(String[] route, OrderingOption ordering, 
			SequencesOption sequenceOption) throws Exception{
		if(Locale.getDefault().equals(Locale.US)){
			return getSequenceData(route,ordering,sequenceOption,MeasurementUnitOption.IMPERIAL);
		}else{
			return getSequenceData(route,ordering,sequenceOption,MeasurementUnitOption.METRIC);
		}
	}
	
	public static StopSequenceRequest getSequenceData(String[] route, OrderingOption ordering, 
			SequencesOption sequenceOption, MeasurementUnitOption measurementUnit) throws Exception{
		return new StopSequenceRequest(route,ordering,sequenceOption,measurementUnit);
	}
	
	private void makeSequenceSet() throws Exception{
		switch(sequenceOption){
			case ALL_SEQUENCES: seqSet = getAllSequenceSet(inputRoute); break;
			case ONLY_ORDERED_SEQUENCES: seqSet = getOrderedSequenceSet(inputRoute); break;
			case BEST_SEQUENCES: seqSet = getAllSequenceSet(inputRoute); break;
		}
	}
	
	private void makeSequenceArray(){
		switch(ordering){
			case BFS_ORDER: sequenceArray = seqSet.getSequencesInBfsOrder(); break;
			case DFS_ORDER: sequenceArray = seqSet.getSequencesInDfsOrder(); break;
			case BEST_SEQUENCES_BY_TIME: sequenceArray = seqSet.getBestSequencesByTime(); break;
			case BEST_SEQUENCES_BY_DISTANCE: sequenceArray = seqSet.getBestSequencesByDistance(); break;
		}
	}
	
	private void makeSequenceDataTable() throws UnsupportedEncodingException{
		sequenceDataTable = new ArrayList<Object[]>(sequenceArray.length);
		for(StopSequence seq: sequenceArray){
			sequenceDataTable.add(getSequenceRow(seq,measurementUnit));
		}
	}
	
	private void makeOutputAddressArray(){
		this.outputAddresses = seqSet.getGoogleData().getAddresses();
	}
	
	public Object[] getSequenceRow(StopSequence seq, MeasurementUnitOption option) throws UnsupportedEncodingException{
		double distanceValue = 0;
		switch(option){
		case METRIC: distanceValue = seq.getAddedDistance().getMetricValue(); break;
		case IMPERIAL: distanceValue = seq.getAddedDistance().getImperialValue(); break;
		}
		
		Object[] toReturn = {
				getLinkDataMap(seq),
				Double.valueOf(seq.getAddedTime().toValue()),
				seq.getAddedTime().toString(),
				distanceValue,
				};
		return toReturn;
	}
	
	private HashMap<String,String> getLinkDataMap(StopSequence seq) throws UnsupportedEncodingException{
		int index = 0;
		String[] addresses = new String[seq.getStopNumbers().size()];
		HashMap<String,String> linkData = new HashMap<String,String>(2);
		
		for(int num:seq.getStopNumbers()){
			addresses[index++] = outputAddresses[num];
		}
		linkData.put("Label", seq.toString());
		linkData.put("URL", GoogleMapsUrlConvertor.getUrlFromAddresses(addresses));
		
		return linkData;
	}
	
	public static StopSequenceSet getOrderedSequenceSet(String[] route) throws Exception{
		return StopSequenceSet.getOrderedSequences(getDistanceMatrixData(route));
	}
	
	public static StopSequenceSet getAllSequenceSet(String[] route) throws Exception{
		return StopSequenceSet.getAllSequences(getDistanceMatrixData(route));
	}
	
	public static DistanceMatrixData getDistanceMatrixData(String[] route){
		DistanceMatrixApiRequest request = DistanceMatrixApiRequest.makeRequest(route, route);
		return DistanceMatrixData.getDataFromJson(request.execute());
	}
	
}
