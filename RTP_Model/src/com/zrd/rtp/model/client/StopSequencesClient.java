package com.zrd.rtp.model.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.zrd.rtp.model.data.StopSequence;
import com.zrd.rtp.model.data.StopSequenceSet;
import com.zrd.rtp.model.googleClient.DistanceMatrixApiRequest;
import com.zrd.rtp.model.googleData.DistanceMatrixData;

public class StopSequencesClient {
	
	public enum OrderingOption{BFS_ORDER, DFS_ORDER};
	
	public enum SequencesOption{ALL_SEQUENCES,ONLY_ORDERED_SEQUENCES};
	
	public enum MeasurementUnitOption{METRIC,IMPERIAL};
	
	public static List<Object[]> getSequenceDataTable(String[] route) throws Exception{
		return getSequenceDataTable(route,OrderingOption.BFS_ORDER);		
	}
	
	public static List<Object[]> getSequenceDataTable(String[] route, OrderingOption ordering) throws Exception{
		return getSequenceDataTable(route, ordering,SequencesOption.ONLY_ORDERED_SEQUENCES);
	}
	
	public static List<Object[]> getSequenceDataTable(String[] route, OrderingOption ordering, 
			SequencesOption sequenceOption) throws Exception{
		if(Locale.getDefault().equals(Locale.US)){
			return getSequenceDataTable(route,ordering,sequenceOption,MeasurementUnitOption.IMPERIAL);
		}else{
			return getSequenceDataTable(route,ordering,sequenceOption,MeasurementUnitOption.METRIC);
		}
	}
	
	public static List<Object[]> getSequenceDataTable(String[] route, OrderingOption ordering, 
			SequencesOption sequenceOption, MeasurementUnitOption measurementUnit) throws Exception{
		StopSequenceSet seqSet = null;
		switch(sequenceOption){
		case ALL_SEQUENCES: seqSet = getAllSequenceSet(route); break;
		case ONLY_ORDERED_SEQUENCES: seqSet = getOrderedSequenceSet(route); break;
		}
		
		StopSequence[] sequenceArray = null;
		switch(ordering){
		case BFS_ORDER: sequenceArray = seqSet.getSequencesInBfsOrder(); break;
		case DFS_ORDER: sequenceArray = seqSet.getSequencesInDfsOrder(); break;
		}
		
		List<Object[]> toReturn = new ArrayList<Object[]>(sequenceArray.length);
		for(StopSequence seq: sequenceArray){
			toReturn.add(getSequenceRow(seq,measurementUnit));
		}
		
		return toReturn;
	}
	
	public static Object[] getSequenceRow(StopSequence seq, MeasurementUnitOption option){
		double distanceValue = 0;
		switch(option){
		case METRIC: distanceValue = seq.getAddedDistance().getMetricValue(); break;
		case IMPERIAL: distanceValue = seq.getAddedDistance().getImperialValue(); break;
		}
		
		Object[] toReturn = {
				seq.toString(),
				Double.valueOf(seq.getAddedTime().toValue()),
				seq.getAddedTime().toString(),
				distanceValue,
				};
		return toReturn;
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
