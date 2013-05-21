package com.zrd.rtp.fileviewcontroller.fileio;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import com.zrd.rtp.model.client.StopSequencesClient;
import com.zrd.rtp.model.data.StopSequence;
import com.zrd.rtp.model.data.StopSequenceSet;
import com.zrd.rtp.model.googleClient.DistanceMatrixApiRequest;
import com.zrd.rtp.model.googleData.DistanceMatrixData;

public class FileInput {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args){
		Scanner consoleReader = new Scanner(System.in);
		System.out.println("File To Read From:");
		File theFile = new File(consoleReader.nextLine());
		String[] stops = null;
		
		try {
			stops = getStopsFromFile(theFile);
		} catch (FileNotFoundException e) {
			System.out.println("That file was not found");
		}
		
		try {
			StopSequenceSet seqSet = StopSequencesClient.getSequenceSet(stops);
			String[] header = {"Sequence","Added Time (minutes)","Added Time","Added Distance(miles)"};
			int[] types = {Cell.CELL_TYPE_STRING,Cell.CELL_TYPE_NUMERIC,Cell.CELL_TYPE_STRING,Cell.CELL_TYPE_NUMERIC};
			List<Object[]> body = new ArrayList<Object[]>();
			for(StopSequence seq:seqSet.getSequencesInBfsOrder()){
				body.add(generateRowFromData(seq));
			}
			String outputExcelFile = theFile.getParentFile().toPath().resolve("outputResponse.xls").toString();
			ExcelFile output = ExcelFile.initXLSWorkbook();
			output.addSheet("Sequence Data", body, types, header);
			output.writeWorkbookToFile(new File(outputExcelFile));
			
		} catch (Exception e1) {
			System.out.println("Error trying to obtain set");
			e1.printStackTrace();
		}

	}
	
	public static Object[] generateRowFromData(StopSequence seq){
		Object[] toReturn = new Object[4];
		toReturn[0] = seq.toString();
		toReturn[1] = Double.valueOf(seq.getAddedTime().toValue());
		toReturn[2] = seq.getAddedTime().toString();
		toReturn[3] = Double.valueOf(seq.getAddedDistance().getImperialValue());
		return toReturn;
	}
	
	public static void printSequences(StopSequenceSet sequenceData){
		System.out.println();
		for(StopSequence seq:sequenceData.getSequencesInBfsOrder()){
			System.out.println(seq.toString()+ " : " + seq.getAddedDistance() + " : " + seq.getAddedTime());
		}
	}
	
	public static String[] getStopsFromFile(File theFile) throws FileNotFoundException{
		ArrayList<String> stops = new ArrayList<String>();
		Scanner fileReader = new Scanner(theFile);
		
		System.out.println("File is: " + theFile.getAbsolutePath());
		System.out.println();
		
		while(fileReader.hasNext()){
			stops.add(fileReader.nextLine());
		}
		
		System.out.println("Stops are:");
		for(String destination: stops){
			System.out.println(destination);
		}
		
		return stops.toArray(new String[stops.size()]);
	}

}
