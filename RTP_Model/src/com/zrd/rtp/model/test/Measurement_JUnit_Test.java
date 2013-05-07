package com.zrd.rtp.model.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zrd.rtp.model.measurements.Distance;
import com.zrd.rtp.model.measurements.Duration;
import com.zrd.rtp.model.measurements.Velocity;

public class Measurement_JUnit_Test {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		
		testDistance(61,120);
		testDistance(59,300);
		testDistance(120,500);
		
		/*
		Distance myDist = Distance.constructUsingMeters(130*1609.34);
		
		Duration myDur = Duration.constructUsingSeconds(19000.54434);
		
		Velocity mySpeed = Velocity.constructUsingMiPerHour(65);
		
		assertEquals(myDist.getImperialText(),"130 mi");
		assertEquals(myDur.toString(),"34 hrs");
		assertEquals(myDur.getImperialText(),"65 mi/hr");
		
		assert myDur.getImperialText().isEmpty();*/
	}
	
	/**
	 * This tests an input of a number of miles and a number of kilometers. 
	 * 		The distance represented by numKmTest MUST be greater than the distance
	 * 		represented by numMileTest
	 * @param numMileTest
	 * @param numKmTest
	 */
	public static void testDistance(int numMileTest, int numKmTest){
		//Distance tests
		Distance testMileDistance = Distance.constructUsingMeters(numMileTest*1609.34);
		assertEquals(testMileDistance.getImperialText(),numMileTest + " mi");
		assertEquals(testMileDistance.getRoundedValue(),(int)Math.round(numMileTest*1609.34));
		
		Distance testKmDistance = Distance.constructUsingMeters(numKmTest*1000);
		assertEquals(testKmDistance.getMetricText(),numKmTest + " km");
		assertEquals((int)testKmDistance.getValue(),numKmTest*1000);
		
		assertEquals(testMileDistance.compareTo(testKmDistance),-1);
		assertEquals(testKmDistance.compareTo(testMileDistance),1);
		
		Distance combined = (Distance) testMileDistance.add(testKmDistance);
		assertEquals(combined.getRoundedValue(), (int)(Math.round(numMileTest*1609.34 + numKmTest*1000) ) ); //value should be 218169.74
		
		Distance backToMileDistance = (Distance) combined.subtract(testKmDistance);
		assertEquals(backToMileDistance.toString(),numMileTest + " mi");
		
		Distance sameAsCombined = Distance.constructUsingMeters(numMileTest*1609.34 + numKmTest*1000);
		assertEquals(sameAsCombined.equals(combined),true);
	}

}
