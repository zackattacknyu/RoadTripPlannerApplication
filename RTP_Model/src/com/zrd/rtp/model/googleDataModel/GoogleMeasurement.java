package com.zrd.rtp.model.googleDataModel;

public class GoogleMeasurement {

	private String text;
	private int value;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getValue() {
		return value;
	}
	public GoogleMeasurement(String text, int value) {
		this.text = text;
		this.value = value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
