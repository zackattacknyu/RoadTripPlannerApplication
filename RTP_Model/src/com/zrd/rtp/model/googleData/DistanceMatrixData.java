package com.zrd.rtp.model.googleData;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DistanceMatrixData {

	private DistanceMatrixElement[][] matrix;
	private String status;
	private String[] addresses;
	private boolean goodRequest;
	private int numberStops;
	
	private DistanceMatrixData(String status){
		this.status = status;
		goodRequest = false;
	}
	
	private DistanceMatrixData(int numberRows){
		this.status = "OK";
		goodRequest = true;
		matrix = new DistanceMatrixElement[numberRows][];
		addresses = new String[numberRows];
		numberStops = numberRows - 2;
	}
	
	public String[] getAddresses() {
		return addresses;
	}

	private void setAddress(int index, String address) {
		addresses[index] = address;
	}

	public int getNumberStops() {
		return numberStops;
	}
	
	public DistanceMatrixElement getElement(int originNumber, int destinationNumber){
		return matrix[originNumber][destinationNumber];
	}
	
	public DistanceMatrixElement getBaseElement(){
		int endNumber = matrix[0].length-1;
		return matrix[0][endNumber];
	}

	private void initializeMatrixRow(int rowIndex, int numberColumns){
		matrix[rowIndex] = new DistanceMatrixElement[numberColumns];
	}
	
	private void initializeMatrixElement(JsonObject matrixElement, int rowIndex, int columnIndex){
		matrix[rowIndex][columnIndex] = DistanceMatrixElement.getDataFromJson(matrixElement);
	}
	
	public static DistanceMatrixData getDataFromJson(JsonObject rootObject){
		String status = rootObject.get("status").getAsString();
		JsonArray originRows, destinationElements,addressElements;
		DistanceMatrixData returnData;
		int rowIndex, columnIndex,addressIndex;
		
		if(status.equals("OK")){
			originRows = rootObject.get("rows").getAsJsonArray();
			returnData = new DistanceMatrixData(originRows.size());
			addressIndex = 0;
			for(JsonElement address: rootObject.get("origin_addresses").getAsJsonArray()){
				returnData.setAddress(addressIndex++, address.getAsString());
			}
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
