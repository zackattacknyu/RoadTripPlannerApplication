package com.zrd.rtp.fileviewcontroller.fileio;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import com.zrd.rtp.model.client.StopSequenceRequest;
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
		
		FileFilter textFilesOnly = new FileNameExtensionFilter("Text Files","txt");
		JFileChooser textFileChooser = new JFileChooser();
		textFileChooser.setFileFilter(textFilesOnly);
		int result = textFileChooser.showOpenDialog(null);
		if(result == JFileChooser.CANCEL_OPTION){
			return;
		}
		File inputTextFile = textFileChooser.getSelectedFile();
		
		FileFilter excelFilesOnly = new FileNameExtensionFilter("Excel Files","xls","xlsx");
		JFileChooser excelFileChooser = new JFileChooser(inputTextFile);
		excelFileChooser.setFileFilter(excelFilesOnly);
		result = excelFileChooser.showSaveDialog(null);
		if(result == JFileChooser.CANCEL_OPTION){
			return;
		}
		File outputExcelFile = excelFileChooser.getSelectedFile();
		String[] stops = null;
		String options = JOptionPane.showInputDialog("Any options? \nType -DfsOrder for the sequences to" + 
				" display in DFS instead of BFS order. \n" + 
				"Type -AllSequences to display all the " + 
				"possible stop sequences instead of just the ordered ones.\n" + 
				"Type -BestSequences or -BestSequencesByTime to display the best order to visit each set of stops by time\n" + 
				"Type -BestSequencesByDistance to display the best order to visit each set of stops by distance");
		if(String.valueOf(options).equals("null")) return;
		
		try {
			stops = getStopsFromFile(inputTextFile);
		} catch (FileNotFoundException e) {
			System.out.println("That file was not found");
		}
		
		StopSequenceRequest.OrderingOption ordering = StopSequenceRequest.OrderingOption.BFS_ORDER;
		StopSequenceRequest.SequencesOption sequences = StopSequenceRequest.SequencesOption.ONLY_ORDERED_SEQUENCES;
		if(options.toLowerCase().contains("-dfsorder")) ordering = StopSequenceRequest.OrderingOption.DFS_ORDER;
		if(options.toLowerCase().contains("-allsequences")) sequences = StopSequenceRequest.SequencesOption.ALL_SEQUENCES;
		if(options.toLowerCase().contains("-bestsequences")){
			sequences = StopSequenceRequest.SequencesOption.BEST_SEQUENCES;
			if(options.toLowerCase().contains("-bestsequencesbydistance")){
				ordering = StopSequenceRequest.OrderingOption.BEST_SEQUENCES_BY_DISTANCE;
			}else{
				ordering = StopSequenceRequest.OrderingOption.BEST_SEQUENCES_BY_TIME;
			}
		}
		
		System.out.println("Attempting to generate excel file");
		FileOutput.generateExcelFile(stops, outputExcelFile,ordering, sequences);
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
