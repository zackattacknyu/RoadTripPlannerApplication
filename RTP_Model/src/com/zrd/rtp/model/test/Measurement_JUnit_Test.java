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
		
		testVelocity(34,120,30,2);
		testVelocity(34,120,44,2);
		testVelocity(34,120,45,2);
		
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
	
	/**
	 * This tests velocities
	 * @param slowMPH		the miles per hour for a "slow" speed. must be slower than fastKmPH
	 * @param fastKmPH		the km/hr for a "fast" speed which MUST be faster than slowMPH
	 * @param numMeters		number of meters for a third speed
	 * @param numSeconds	number of seconds for the third speed
	 */
	public static void testVelocity(int slowMPH, int fastKmPH, int numMeters,int numSeconds){

		Velocity slowOne = Velocity.constructUsingMiPerHour(slowMPH);
		assertEquals(slowOne.getImperialText(),slowMPH + " mi/hr");
		assertEquals(slowOne.toString(),slowMPH + " mi/hr");
		assertEquals(slowOne.getRoundedValue(),(int)Math.round((slowMPH*1609.34)/3600));
		
		Velocity fastOne = Velocity.constructUsingKmPerHour(fastKmPH);
		assertEquals(fastOne.getMetricText(),fastKmPH + " km/hr");
		assertEquals((int)fastOne.getValue(),(int)Math.round((fastKmPH*1000)/3600));
		
		assertEquals(fastOne.compareTo(slowOne),1);
		assertEquals(slowOne.compareTo(fastOne),-1);
		
		Velocity otherVeloc = Velocity.constructUsingMetersAndSeconds(numMeters, numSeconds);
		assertEquals(otherVeloc.getRoundedValue(),(int)Math.round(numMeters/numSeconds));

		boolean exceptionThrown = false;
		try{
			slowOne.add(fastOne);
		}catch(UnsupportedOperationException e){
			exceptionThrown = true;
		}
		assertEquals(exceptionThrown,true);
		
		exceptionThrown = false;
		try{
			fastOne.subtract(slowOne);
		}catch(UnsupportedOperationException e){
			exceptionThrown = true;
		}
		assertEquals(exceptionThrown,true);
		
	}

}
