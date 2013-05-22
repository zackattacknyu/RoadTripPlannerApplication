package com.zrd.rtp.fileviewcontroller.fileio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

import com.zrd.rtp.model.client.StopSequencesClient;
import com.zrd.rtp.model.data.StopSequence;
import com.zrd.rtp.model.data.StopSequenceSet;

public class FileOutput {

	
	public static void generateExcelFile(String[] stops, File excelFile){
		
		try{
			String[] header = {"Sequence","Added Time (minutes)","Added Time","Added Distance(miles)"};
			int[] types = {Cell.CELL_TYPE_STRING,Cell.CELL_TYPE_NUMERIC,Cell.CELL_TYPE_STRING,Cell.CELL_TYPE_NUMERIC};
			ExcelFile output = ExcelFile.initXLSWorkbook();
			output.addSheet("Sequence Data", 
					StopSequencesClient.getSequenceDataTable(stops,StopSequencesClient.OrderingOption.BFS_ORDER,StopSequencesClient.SequencesOption.ALL_SEQUENCES), 
					types, header);
			output.writeWorkbookToFile(excelFile);
		}catch(Exception e){
			System.out.println("Error trying to obtain set");
			e.printStackTrace();
		}
		
	}
	
}
