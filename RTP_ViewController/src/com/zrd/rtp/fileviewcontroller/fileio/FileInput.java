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
		
		FileOutput.generateExcelFile(stops, theFile.getParentFile().toPath().resolve("result.xls").toFile());

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
