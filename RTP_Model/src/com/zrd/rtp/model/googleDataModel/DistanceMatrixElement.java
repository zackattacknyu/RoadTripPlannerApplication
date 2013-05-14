package com.zrd.rtp.model.googleDataModel;

import com.google.gson.JsonObject;

public class DistanceMatrixElement {

	private GoogleMeasurement distance;
	private GoogleMeasurement duration;
	private String status;
	private boolean validRoute;
	
	private DistanceMatrixElement(String status) {
		this.status = status;
		validRoute = false;
	}
	private DistanceMatrixElement(GoogleMeasurement distance,
			GoogleMeasurement duration) {
		validRoute = true;
		this.distance = distance;
		this.duration = duration;
		this.status = "OK";
	}
	
	public GoogleMeasurement getDistance() {
		return distance;
	}
	public GoogleMeasurement getDuration() {
		return duration;
	}
	public String getStatus() {
		return status;
	}
	public boolean isValidRoute() {
		return validRoute;
	}
	public static DistanceMatrixElement getDataFromJson(JsonObject rootObject){
		String status = rootObject.get("status").getAsString();
		GoogleMeasurement distanceMeasure, durationMeasure;
		
		if(status.equals("OK")){
			distanceMeasure = GoogleMeasurement.getDataFromJson(rootObject.get("distance").getAsJsonObject());
			durationMeasure = GoogleMeasurement.getDataFromJson(rootObject.get("duration").getAsJsonObject());
			return new DistanceMatrixElement(distanceMeasure,durationMeasure);
		}else{
			return new DistanceMatrixElement(status);
		}
	}
}
