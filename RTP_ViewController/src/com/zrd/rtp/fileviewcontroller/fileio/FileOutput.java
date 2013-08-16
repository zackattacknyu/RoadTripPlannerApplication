package com.zrd.rtp.fileviewcontroller.fileio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

import com.zrd.rtp.model.client.StopSequenceRequest;
import com.zrd.rtp.model.data.StopSequence;
import com.zrd.rtp.model.data.StopSequenceSet;

public class FileOutput {

	public static void generateExcelFile(String[] stops, File excelFile){
		generateExcelFile(stops, excelFile, StopSequenceRequest.OrderingOption.BFS_ORDER, StopSequenceRequest.SequencesOption.ONLY_ORDERED_SEQUENCES);
	}
	
	
	public static void generateExcelFile(String[] stops, File excelFile, StopSequenceRequest.OrderingOption ordering, StopSequenceRequest.SequencesOption sequences){
		
		try{
			ExcelFile output = ExcelFile.initXLSWorkbook();
			StopSequenceRequest request = StopSequenceRequest.getSequenceData(stops, ordering, sequences);
			
			String[] infoHeader = {"Sequence Number","Address"};
			int[] infoTypes = {Cell.CELL_TYPE_NUMERIC,Cell.CELL_TYPE_STRING};
			output.addSheet("Sequence Legend", request.getOutputAddresses(), infoTypes, infoHeader);
			
			String[] header = {"Sequence","Added Time (minutes)","Added Time","Added Distance(miles)"};
			int[] types = {Integer.MAX_VALUE,Cell.CELL_TYPE_NUMERIC,Cell.CELL_TYPE_STRING,Cell.CELL_TYPE_NUMERIC};
			output.addSheet("Sequence Data", 
					request.getSequenceDataTable(), 
					types, header);
			
			output.writeWorkbookToFile(excelFile);
		}catch(Exception e){
			System.out.println("Error trying to obtain set");
			e.printStackTrace();
		}
		
	}
	
	
}
