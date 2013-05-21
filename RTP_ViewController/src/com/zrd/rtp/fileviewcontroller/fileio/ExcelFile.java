package com.zrd.rtp.fileviewcontroller.fileio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFile {

	/**
	 * This is used to get the cell style of the body cells. 
	 * If you want a custom style for the cells that say the attribute values
	 *          of the entries in the list, customize this method. 
	 * @param theWorkbook       the Excel workbook we are working in
	 * @return          the cell style for the body
	 */
	public static CellStyle GetBodyCellStyle(Workbook theWorkbook){
	    
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
	public static CellStyle GetHeaderCellStyle(Workbook theWorkbook){
	    
	    CellStyle HeaderStyle = theWorkbook.createCellStyle();
	    Font HeaderFont = theWorkbook.createFont();
	    
	    /*sets the style and font appropriately
	     * NOTE: This is the code block where customization should be done 
	     */
	    HeaderFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    HeaderStyle.setFont(HeaderFont);
	    
	    
	    return HeaderStyle;
	}
	
	private Workbook workbook;
	private CellStyle headerStyle;
	private CellStyle bodyStyle;
	
	private ExcelFile(Workbook workbook){
		this.workbook = workbook;
		this.headerStyle = GetHeaderCellStyle(workbook);
		this.bodyStyle = GetBodyCellStyle(workbook);
	}
	
	public void addSheet(String sheetName, List<Object[]> bodyData, int[] entryTypes, String[] header){
		Sheet addedSheet = workbook.createSheet(sheetName);
		
		createHeaderRow(addedSheet.createRow(0),header);
		
		//goes through each entry in the list creating the rows
	    for(int index = 0; index < bodyData.size(); index++){
	    	createBodyRow(addedSheet.createRow(index + 1),bodyData.get(index),entryTypes); //the 1 accounts for the header
	    }
	    
	    //it now auto-sizes the columns
	    for(int column = 0; column <= header.length; column++){
	    	addedSheet.autoSizeColumn(column);
	    }
	    
	}
	
	private void createBodyRow(Row currentRow, Object[] bodyRow, int[] types){
		Cell currentCell;
		
		//write the section attributes to the row
	    for(int index = 0; index < bodyRow.length; index++){
	        //create the cell
            currentCell = currentRow.createCell(index);
            
            //set the format of the cells
            currentCell.setCellStyle(bodyStyle);
            
            currentCell.setCellType(types[index]);
            
            //put the value into the cell
            switch(types[index]){
            case Cell.CELL_TYPE_NUMERIC: 
            	currentCell.setCellValue((double)bodyRow[index]); break;
            case Cell.CELL_TYPE_FORMULA:
            	currentCell.setCellFormula(String.valueOf(bodyRow[index])); break;
            case Integer.MAX_VALUE:
            	currentCell.setHyperlink((Hyperlink)bodyRow[index]); break; //MAX_VALUE integer will be used to denote hyperlinks
            case Cell.CELL_TYPE_STRING:
            	currentCell.setCellValue(String.valueOf(bodyRow[index])); break;
            default:
            	throw new UnsupportedOperationException("The type " + types[index] + " is not supported." + 
            			" Only types 0, 1, 2, and Integer.MAX_VALUE (for hyperlinks)" + 
            			" are supported. See the javadoc for org.apache.poi.ss.usermodel.Cell " + 
            			"for more information on the types of cells.");
            }
	    }
	}
	
	/**
	 * This method creates the header row in the workbook object
	 * @param currentRow                the object representing the header row
	 * @param theWorkbook               the object representing the workbook, which will be modified by this method
	 */
	private void createHeaderRow(Row currentRow, String[] header){
	    Cell currentCell;
	    
	    //write the section attributes to the row
	    for(int index = 0; index < header.length; index++){
	        currentCell = currentRow.createCell(index);
	        
	        currentCell.setCellStyle(headerStyle);
	        currentCell.setCellValue(header[index]);
	    }
	    
	
	}
	
	public static ExcelFile initXLSWorkbook(){
		return new ExcelFile(new HSSFWorkbook());
	}
	
	public void writeWorkbookToFile(File excelFile){
		FileOutputStream excelOutputStream = null;
		
		try{
			excelOutputStream = new FileOutputStream(excelFile);
			workbook.write(excelOutputStream);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(excelOutputStream != null){
				try{
					excelOutputStream.flush();
					excelOutputStream.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
