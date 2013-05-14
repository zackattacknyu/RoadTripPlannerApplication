package com.zrd.rtp.model.googleDataModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DistanceMatrixData {

	private DistanceMatrixElement[][] matrix;
	private String status;
	private boolean goodRequest;
	
	private DistanceMatrixData(String status){
		this.status = status;
		goodRequest = false;
	}
	
	private DistanceMatrixData(int numberRows){
		this.status = "OK";
		goodRequest = true;
		matrix = new DistanceMatrixElement[numberRows][];
	}
	
	private void initializeMatrixRow(int rowIndex, int numberColumns){
		matrix[rowIndex] = new DistanceMatrixElement[numberColumns];
	}
	
	private void initializeMatrixElement(JsonObject matrixElement, int rowIndex, int columnIndex){
		matrix[rowIndex][columnIndex] = DistanceMatrixElement.getDataFromJson(matrixElement);
	}
	
	public static DistanceMatrixData getDataFromJson(JsonObject rootObject){
		String status = rootObject.get("status").getAsString();
		JsonArray originRows, destinationElements;
		DistanceMatrixData returnData;
		int rowIndex, columnIndex;
		
		if(status.equals("OK")){
			originRows = rootObject.get("rows").getAsJsonArray();
			returnData = new DistanceMatrixData(originRows.size());
			rowIndex = 0;
			for(JsonElement currentRow:originRows){
				destinationElements = currentRow.getAsJsonObject().get("elements").getAsJsonArray();
				returnData.initializeMatrixRow(rowIndex, destinationElements.size());
				columnIndex = 0;
				for(JsonElement element:destinationElements){
					returnData.initializeMatrixElement(element.getAsJsonObject(),rowIndex,columnIndex);
					columnIndex++;
				}
				rowIndex++;
			}
			
			return returnData;
		}else{
			return new DistanceMatrixData(status);
		}
	}

	public DistanceMatrixElement[][] getMatrix() {
		return matrix;
	}

	public String getStatus() {
		return status;
	}

	public boolean isGoodRequest() {
		return goodRequest;
	}
}
