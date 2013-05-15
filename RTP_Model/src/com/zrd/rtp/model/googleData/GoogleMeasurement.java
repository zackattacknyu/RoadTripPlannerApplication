package com.zrd.rtp.model.googleData;

import com.google.gson.JsonObject;

public class GoogleMeasurement {

	private String text;
	private int value;
	public String getText() {
		return text;
	}
	public int getValue() {
		return value;
	}
	private GoogleMeasurement(String text, int value) {
		this.text = text;
		this.value = value;
	}
	
	public static GoogleMeasurement getDataFromJson(JsonObject measurementData){
		String text = measurementData.get("text").getAsString();
		int value = measurementData.get("value").getAsInt();
		return new GoogleMeasurement(text,value);
	}
}
