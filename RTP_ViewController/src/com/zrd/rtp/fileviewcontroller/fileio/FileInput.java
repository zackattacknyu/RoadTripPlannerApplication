package com.zrd.rtp.fileviewcontroller.fileio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.zrd.rtp.model.client.GoogleMapsUrlConvertor;
import com.zrd.rtp.model.client.StopSequenceRequest;

public class FileInput {
	
	public static final String initialGreeting = 
			"Enter the Google Maps URL or hit " +
			"Cancel to specify a text file";
	
	public static final String optionsDialog = "Any options? By default, this displays the best order to visit each set of stops by time.\n" + 
			"Type -ByDistance to display the best sequences by distance instead of time.\n" +
			"Type -DfsOrder or -BfsOrder for the sequences to display in those orders instead of the default. \n" + 
			"Type -AllSequences to display all the possible stop sequences instead of just the best ones.\n" +
			"Type -OrderedSequences to display all the ordered sequences instead of the best sequences";

	/**
	 * @param args
	 * @throws URISyntaxException 
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException, URISyntaxException, FileNotFoundException{
		
		String googleUrl = JOptionPane.showInputDialog(initialGreeting);
		String[] stops = null;
		File inputTextFile = null;
		if(googleUrl == null){
			inputTextFile = obtainTextFileFromDialog();
			if(inputTextFile == null) return;
			stops = getStopsFromFile(inputTextFile);
		}else{
			stops = GoogleMapsUrlConvertor.getAddressesFromURL(googleUrl);
		}
		
		File outputExcelFile = obtainExcelFileFromDialog(inputTextFile);
		if(outputExcelFile == null) return;
		
		String options = JOptionPane.showInputDialog(optionsDialog);
		if(String.valueOf(options).equals("null")) return;

		StopSequenceRequest.OrderingOption ordering = StopSequenceRequest.OrderingOption.BEST_SEQUENCES_BY_TIME;
		StopSequenceRequest.SequencesOption sequences = StopSequenceRequest.SequencesOption.BEST_SEQUENCES;
		if(options.toLowerCase().contains("-bydistance")) ordering = StopSequenceRequest.OrderingOption.BEST_SEQUENCES_BY_DISTANCE;
		
		if(options.toLowerCase().contains("-dfsorder")) ordering = StopSequenceRequest.OrderingOption.DFS_ORDER;
		if(options.toLowerCase().contains("-bfsorder")) ordering = StopSequenceRequest.OrderingOption.BFS_ORDER;
		if(options.toLowerCase().contains("-allsequences")) sequences = StopSequenceRequest.SequencesOption.ALL_SEQUENCES;
		if(options.toLowerCase().contains("-orderedsequences")) sequences = StopSequenceRequest.SequencesOption.ONLY_ORDERED_SEQUENCES;
		
		System.out.println("Attempting to generate excel file");
		FileOutput.generateExcelFile(stops, outputExcelFile,ordering, sequences);
		

	}
	
	private static File obtainExcelFileFromDialog(File startingFolder){
		FileFilter excelFilesOnly = new FileNameExtensionFilter("Excel Files","xls","xlsx");
		JFileChooser excelFileChooser = new JFileChooser(startingFolder);
		excelFileChooser.setFileFilter(excelFilesOnly);
		int result = excelFileChooser.showSaveDialog(null);
		if(result == JFileChooser.CANCEL_OPTION){
			return null;
		}
		return excelFileChooser.getSelectedFile();
		
	}
	
	private static File obtainTextFileFromDialog(){
		FileFilter textFilesOnly = new FileNameExtensionFilter("Text Files","txt");
		JFileChooser textFileChooser = new JFileChooser();
		textFileChooser.setFileFilter(textFilesOnly);
		int result = textFileChooser.showOpenDialog(null);
		if(result == JFileChooser.CANCEL_OPTION){
			return null;
		}
		return textFileChooser.getSelectedFile();
		
	}
	
	
	private static String[] getStopsFromFile(File theFile) throws FileNotFoundException{
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
		
		fileReader.close();
		
		return stops.toArray(new String[stops.size()]);
	}

}
