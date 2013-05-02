package com.zrd.rtp.model.testClasses;

import com.zrd.rtp.model.measurements.Distance;
import com.zrd.rtp.model.measurements.Duration;
import com.zrd.rtp.model.measurements.Velocity;

public class MeasurementTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Distance myDist = Distance.constructUsingMeters(130*1609.34);
		Distance myDist2 = Distance.constructUsingMeters(545000.99);
		Distance myDist3 = Distance.constructUsingMeters(544998);
		System.out.println("my distance in kilometers:" + myDist.getMetricText());
		System.out.println("my distance in miles:" + myDist.getImperialText());
		System.out.println("my distance: " + myDist.toString());
		System.out.println("my distance2: " + myDist2.toString());
		System.out.println("my distance3: " + myDist3.toString());
		System.out.println("dist 1 or dist 2: " + myDist.compareTo(myDist2));
		System.out.println("dist 1 or dist 3: " + myDist.compareTo(myDist3));
		
		
		Duration myDur = Duration.constructUsingSeconds(19000.54434);
		System.out.println("Duration: " + myDur.toString());
		
		Velocity mySpeed = Velocity.constructUsingMiPerHour(65);
		System.out.println("My speed in m/sec: " + mySpeed.getRoundedValue());
		System.out.println("My speed: " + mySpeed.getMetricText());
		System.out.println("My speed: " + mySpeed.toString());
		
		Duration timeElapsed = Duration.constructUsingVelocityAndDistance(mySpeed, myDist);
		System.out.println("it takes " + timeElapsed.toString());
		
		Duration baseDuration = Duration.constructUsingSeconds(2*3600 + 35*60);
		Duration newDuration = Duration.constructUsingSeconds(2*3600 + 50*60);
		Duration addedTime = (Duration)newDuration.subtract(baseDuration);
		Duration totalTime = (Duration)newDuration.add(baseDuration);
		System.out.println("Difference is " + addedTime.toString());
		System.out.println("Total is " + totalTime.toString());
	}

}
