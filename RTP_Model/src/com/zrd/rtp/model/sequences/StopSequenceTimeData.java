package com.zrd.rtp.model.sequences;

import com.zrd.rtp.model.measurements.Duration;


public class StopSequenceTimeData implements Comparable<StopSequenceTimeData> {

	private String sequenceKey;
	private Duration addedTime;
	public String getSequenceKey() {
		return sequenceKey;
	}
	public Duration getAddedTime() {
		return addedTime;
	}
	public StopSequenceTimeData(String sequenceKey, Duration addedTime) {
		super();
		this.sequenceKey = sequenceKey;
		this.addedTime = addedTime;
	}
	public void setAddedTime(Duration addedTime) {
		this.addedTime = addedTime;
	}
	@Override
	public int compareTo(StopSequenceTimeData arg0) {
		return addedTime.compareTo(arg0.getAddedTime());
	}
}
