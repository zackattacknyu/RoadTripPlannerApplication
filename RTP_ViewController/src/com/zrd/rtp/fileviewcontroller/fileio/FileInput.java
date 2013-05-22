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
		System.out.println("Name of Resultant Excel File: ");
		String fileName = consoleReader.nextLine();
		String[] stops = null;
		
		try {
			stops = getStopsFromFile(theFile);
		} catch (FileNotFoundException e) {
			System.out.println("That file was not found");
		}
		
		System.out.println("Attempting to generate excel file");
		FileOutput.generateExcelFile(stops, theFile.getParentFile().toPath().resolve(fileName + ".xls").toFile());
		System.out.println("Successfuly generated excel file");

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
