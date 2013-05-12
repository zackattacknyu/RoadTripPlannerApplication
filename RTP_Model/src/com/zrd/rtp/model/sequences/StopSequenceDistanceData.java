package com.zrd.rtp.model.sequences;

import com.zrd.rtp.model.measurements.Distance;

public class StopSequenceDistanceData implements Comparable<StopSequenceDistanceData>{

	private String sequenceKey;
	public Distance getAddedDistance() {
		return addedDistance;
	}

	public void setAddedDistance(Distance addedDistance) {
		this.addedDistance = addedDistance;
	}

	public String getSequenceKey() {
		return sequenceKey;
	}

	public StopSequenceDistanceData(String sequenceKey, Distance addedDistance) {
		super();
		this.sequenceKey = sequenceKey;
		this.addedDistance = addedDistance;
	}

	private Distance addedDistance;
	
	@Override
	public int compareTo(StopSequenceDistanceData o) {
		return addedDistance.compareTo(o.getAddedDistance());
	}

}
