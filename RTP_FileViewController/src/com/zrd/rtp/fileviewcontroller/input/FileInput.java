package com.zrd.rtp.fileviewcontroller.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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
		String fullFilePath = consoleReader.nextLine();
		String[] stops = null;
		
		try {
			stops = getStopsFromFile(fullFilePath);
		} catch (FileNotFoundException e) {
			System.out.println("That file was not found");
		}

		try {
			printSequences(StopSequencesClient.getSequenceSet(stops));
		} catch (Exception e) {
			System.out.println("Error trying to obtain set");
		}
	}
	
	public static void printSequences(StopSequenceSet sequenceData){
		System.out.println();
		for(StopSequence seq:sequenceData.getSequencesInBfsOrder()){
			System.out.println(seq.toString()+ " : " + seq.getAddedDistance() + " : " + seq.getAddedTime());
		}
	}
	
	public static String[] getStopsFromFile(String fullFilePath) throws FileNotFoundException{
		ArrayList<String> stops = new ArrayList<String>();
		Scanner fileReader = new Scanner(new File(fullFilePath));
		
		System.out.println("File is: " + fullFilePath);
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
