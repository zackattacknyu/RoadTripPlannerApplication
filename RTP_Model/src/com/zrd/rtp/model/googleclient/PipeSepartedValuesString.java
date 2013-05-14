package com.zrd.rtp.model.googleClient;

public class PipeSepartedValuesString {

	private StringBuilder currentString;
	
	public PipeSepartedValuesString(String firstString){
		currentString = new StringBuilder(firstString);
	}
	
	public void addString(String stringToAdd){
		currentString.append("|");
		currentString.append(stringToAdd);
	}

	@Override
	public String toString() {
		return currentString.toString();
	}
	
	public static String getString(String[] array){
		if(array.length < 1){
			return "";
		}else if(array.length < 2){
			return array[0];
		}else{
			PipeSepartedValuesString arrayStr = new PipeSepartedValuesString(array[0]);
			for(int index = 1; index < array.length; index++){
				arrayStr.addString(array[index]);
			}
			return arrayStr.toString();
		}
	}
	
	
}
