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
		
		testDuration(45,50,28,60,120,100,200);
	}
	
	/**
	 * This is a test of the duration class
	 * @param numSeconds			a sample number of seconds
	 * @param numMinutes			a sample number of minutes < 60 and > 1
	 * @param numHours				a sample number of hours > 1
	 * @param numMilesPerHour		sample speed in miles per hour
	 * @param numMiles				sample number of miles that is a multiple of numMilesPerHour
	 * @param numKmPerHour			sample speed in km per hour
	 * @param numKm					sample number of kilometers that is a multiple of kmPerHour
	 */
	public static void testDuration(int numSeconds, int numMinutes, int numHours, int numMilesPerHour, int numMiles, int numKmPerHour, int numKm){
		
		Duration testInitDuration = Duration.constructUsingSeconds(numSeconds);
		boolean exceptionThrown = false;
		try{
			testInitDuration.getImperialText();
		}catch(UnsupportedOperationException e){
			exceptionThrown = true;
		}
		assertEquals(exceptionThrown,true);
		
		exceptionThrown = false;
		try{
			testInitDuration.getMetricText();
		}catch(UnsupportedOperationException e){
			exceptionThrown = true;
		}
		assertEquals(exceptionThrown,true);
		
		Velocity mph = Velocity.constructUsingMiPerHour(numMilesPerHour);
		Distance miles = Distance.constructUsingMeters(numMiles*1609.34);
		Duration mileDuration = Duration.constructUsingVelocityAndDistance(mph, miles);
		assertEquals(mileDuration.toString(),(numMiles/numMilesPerHour) + " hrs");
		
		Velocity kmph = Velocity.constructUsingKmPerHour(numKmPerHour);
		Distance kilometers = Distance.constructUsingMeters(numKm*1000);
		Duration kmDuration = Duration.constructUsingVelocityAndDistance(kmph, kilometers);
		assertEquals(kmDuration.toString(),(numKm/numKmPerHour) + " hrs");
		
		
		Duration testOneMinute = Duration.constructUsingSeconds(60);
		assertEquals(testOneMinute.toString(),"1 min");
		
		Duration testMinutes = Duration.constructUsingSeconds(numMinutes*60);
		assertEquals(testMinutes.toString(),numMinutes + " mins");
				
		Duration testOneHour = Duration.constructUsingSeconds(3600);
		assertEquals(testOneHour.toString(),"1 hr");
		
		Duration testOneHourOneMin = Duration.constructUsingSeconds(3600+60);
		assertEquals(testOneHourOneMin.toString(), "1 hr 1 min");
		
		Duration testOneHourAndMins = Duration.constructUsingSeconds(3600 + numMinutes*60);
		assertEquals(testOneHourAndMins.toString(),"1 hr " + numMinutes + " mins");
		
		Duration testHours = Duration.constructUsingSeconds(numHours*3600);
		assertEquals(testHours.toString(),numHours + " hrs");
		
		Duration testHoursOneMin = Duration.constructUsingSeconds(numHours*3600 + 60);
		assertEquals(testHoursOneMin.toString(),numHours + " hrs 1 min");
		
		Duration testHoursAndMins = Duration.constructUsingSeconds(numHours*3600 + 60*numMinutes);
		assertEquals(testHoursAndMins.toString(),numHours + " hrs " + numMinutes + " mins");
		
		
		
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
		assertTrue(withinRange(testMileDistance.getRoundedValue(),numMileTest*1609.34));
		
		Distance testKmDistance = Distance.constructUsingMeters(numKmTest*1000);
		assertEquals(testKmDistance.getMetricText(),numKmTest + " km");
		assertTrue(withinRange( testKmDistance.getValue(), numKmTest*1000));
		
		assertEquals(testMileDistance.compareTo(testKmDistance),-1);
		assertEquals(testKmDistance.compareTo(testMileDistance),1);
		
		Distance combined = (Distance) testMileDistance.add(testKmDistance);
		assertTrue(withinRange(combined.getRoundedValue() , numMileTest*1609.34 + numKmTest*1000));
		
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
		assertTrue(withinRange(slowOne.getRoundedValue(),(slowMPH*1609.34)/3600));
		
		Velocity fastOne = Velocity.constructUsingKmPerHour(fastKmPH);
		assertEquals(fastOne.getMetricText(),fastKmPH + " km/hr");
		assertTrue(withinRange(fastOne.getValue(),(fastKmPH*1000)/3600));
		
		assertEquals(fastOne.compareTo(slowOne),1);
		assertEquals(slowOne.compareTo(fastOne),-1);
		
		Velocity otherVeloc = Velocity.constructUsingMetersAndSeconds(numMeters, numSeconds);
		assertTrue(withinRange(otherVeloc.getRoundedValue(),numMeters/numSeconds));

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
	
	public static boolean withinRange(double one, double two){
		return (Math.abs(one-two) <= 1);
	}

}
