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
			StopSequenceSet seqSet = StopSequencesClient.getOrderedSequenceSet(stops);
			String[] header = {"Sequence","Added Time (minutes)","Added Time","Added Distance(miles)"};
			int[] types = {Cell.CELL_TYPE_STRING,Cell.CELL_TYPE_NUMERIC,Cell.CELL_TYPE_STRING,Cell.CELL_TYPE_NUMERIC};
			List<Object[]> body = new ArrayList<Object[]>();
			for(StopSequence seq:seqSet.getSequencesInBfsOrder()){
				body.add(generateRowFromData(seq));
			}
			ExcelFile output = ExcelFile.initXLSWorkbook();
			output.addSheet("Sequence Data", body, types, header);
			output.writeWorkbookToFile(excelFile);
		}catch(Exception e){
			System.out.println("Error trying to obtain set");
			e.printStackTrace();
		}
		
	}
	
	private static Object[] generateRowFromData(StopSequence seq){
		Object[] toReturn = new Object[4];
		toReturn[0] = seq.toString();
		toReturn[1] = Double.valueOf(seq.getAddedTime().toValue());
		toReturn[2] = seq.getAddedTime().toString();
		toReturn[3] = Double.valueOf(seq.getAddedDistance().getImperialValue());
		return toReturn;
	}
}
