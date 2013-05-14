package com.zrd.rtp.model.googleDataModel;

public class DistanceMatrixElement {

	private GoogleMeasurement distance;
	private GoogleMeasurement duration;
	private String status;
	public DistanceMatrixElement(String status) {
		this.status = status;
	}
	public DistanceMatrixElement(GoogleMeasurement distance,
			GoogleMeasurement duration, String status) {
		this.distance = distance;
		this.duration = duration;
		this.status = status;
	}
}
