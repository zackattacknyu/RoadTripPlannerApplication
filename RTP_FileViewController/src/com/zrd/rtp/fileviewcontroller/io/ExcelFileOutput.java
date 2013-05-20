package com.zrd.rtp.fileviewcontroller.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelFileOutput {

		/**
		 * This is used to get the cell style of the body cells. 
		 * If you want a custom style for the cells that say the attribute values
		 *          of the entries in the list, customize this method. 
		 * @param theWorkbook       the Excel workbook we are working in
		 * @return          the cell style for the body
		 */
		private static CellStyle GetBodyCellStyle(Workbook theWorkbook){
		    
		    CellStyle theStyle = theWorkbook.createCellStyle();
		    
		    /*set the style to wrap the text and center vertical alignment
		     * NOTE: This is the code block where customization should be done
		     */
		    theStyle.setWrapText(true);
		    theStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		    theStyle.setAlignment(CellStyle.ALIGN_LEFT);
		    
		    return theStyle;
		}
		
		/**
		 * This is used to get the cell style of the header cells. 
		 * If you want a custom style for the column header cells, customize this method. 
		 * @param theWorkbook       the Excel workbook we are working in
		 * @return          the cell style for the header
		 */
		private static CellStyle GetHeaderCellStyle(Workbook theWorkbook){
		    
		    CellStyle HeaderStyle = theWorkbook.createCellStyle();
		    Font HeaderFont = theWorkbook.createFont();
		    
		    /*sets the style and font appropriately
		     * NOTE: This is the code block where customization should be done 
		     */
		    HeaderFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		    HeaderStyle.setFont(HeaderFont);
		    
		    
		    return HeaderStyle;
		}
		
		
		/*
		 * The following are the core components of the Convertor program. 
		 * They should not be modified unless it is necessary for 
		 *          bug fixing or enchancments to the program. 
		 *  
		 */
		
		/**
		 * This is the main method that should be called to convert a list of hashmaps to a file.
		 * It will take the list and make the Excel spreadsheet at the specified fileName
		 * @param fileName          the directory where the file will be written to
		 * @param xmlData           the list of Hashmaps used to get the data to write to excel
		 */
		public static void GenerateExcelFile(String fileName, Workbook theWorkbook, List<String[]> bodyData, String[] header, String sheetName){
		    
		    //write the list to an initial workbook object. ensures that it is compatible. 
		    ListOfRowsToSheet(theWorkbook.createSheet(sheetName), bodyData,header,sheetName);
		    
		    //auto-size the column width
		    AdjustColumnWidth(theWorkbook,header.length,sheetName);
		    
		    //finally write it to a file
		    writeWorkbookToFile(theWorkbook,fileName);
		}
		    
		
		/**
		 * This method converts the list of hashmaps to a workbook object
		 * @param xmlData           the list of Hashmaps which holds the data
		 * @return                          the workbook object generated
		 */
		public static Workbook ListOfRowsToSheet(Sheet theSheet, List<String[]> bodyData, String[] header, String sheetName){
		    Row row;
		    CellStyle bodyCellStyle = GetBodyCellStyle(theSheet.getWorkbook());
		    
		    //writes the header
		    row = theSheet.createRow(0);
		    createHeaderRow(row,theSheet.getWorkbook(),header);
		    
		    //goes through each entry in the list
		    for(int index = 0; index < bodyData.size(); index++){
		    	row = theSheet.createRow(index);
		    	createBodyRow(row,bodyData.get(index),bodyCellStyle);
		    }
		    
		    return theSheet.getWorkbook();
		}
		
		/**
		 * This method creates the header row in the workbook object
		 * @param currentRow                the object representing the header row
		 * @param theWorkbook               the object representing the workbook, which will be modified by this method
		 */
		private static void createHeaderRow(Row currentRow, Workbook theWorkbook, String[] header){
		    Cell currentCell;
		    CellStyle headerStyle = GetHeaderCellStyle(theWorkbook);
		    
		    //write the section attributes to the row
		    for(int index = 0; index < header.length; index++){
		        currentCell = currentRow.createCell(index);
		        
		        currentCell.setCellStyle(headerStyle);
		        currentCell.setCellValue(header[index]);
		    }
		    
		
		}
		
		
		/**
		 * This method converts the individual hashmap in the list to an Excel workbook row
		 * @param currentRow                        the current row we will use for the information
		 * @param currentSection            the current entry in the list of Hashmaps
		 * @param bodyCellStyle                     the style of the body cells that will be used for the spreadsheet
		 * @param labelCellStyle            the style of the lable cells that will be in the spreadsheet
		 */
		private static void createBodyRow(Row currentRow, String[] entries, CellStyle bodyCellStyle){
		    Cell currentCell;
		    
		    
		    //write the section attributes to the row
		    for(int index = 0; index < entries.length; index++){
		        //create the cell
	            currentCell = currentRow.createCell(index);
	            
	            //set the format of the cells
	            currentCell.setCellStyle(bodyCellStyle);
	            
	            //put the value into the cell
	            currentCell.setCellValue(entries[index]);
		    }
		   
		    
		    
		}
		
		/**
		 * This will take in the spreadsheet, number of columns, and sheet name, and then
		 *          auto-size the columns so the text fits in the columns
		 * @param workbook                  the Excel spreadsheet object
		 * @param numColumns                the number of columns to be auto-sized
		 * @param theSheetName              the name of the sheet to be auto-sized
		 */
		public static void AdjustColumnWidth(Workbook workbook, int numColumns, String theSheetName){
		    Sheet theSheet = workbook.getSheet(theSheetName);
		    
		    for(int column = 0; column <= numColumns; column++){
		        theSheet.autoSizeColumn(column);
		    }
		}
		
		/**
		 * This method takes the Excel workbook object and write the .xls file from it. 
		 * @param workbook          the Excel workbook object
		 * @param fileName          the .xls file to write the workbook to
		 */
		public static void writeWorkbookToFile(Workbook workbook, String fileName){
		    
		    File ExcelToMake = new File(fileName);
		    FileOutputStream excelSheet = null;
		    
		    try{
		        excelSheet = new FileOutputStream(ExcelToMake);
		        workbook.write(excelSheet);
		    }  catch (IOException e) {
		        e.printStackTrace();
		    }
		    finally{
		            
		            if (excelSheet != null){
		                try{
		                    excelSheet.flush();
		                    excelSheet.close();
		                } catch (IOException e) {
		                    e.printStackTrace();
		                }
		
		            }
		        }
		
		}
}
