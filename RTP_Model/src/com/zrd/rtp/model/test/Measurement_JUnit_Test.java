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
		
		for(int numTry = 0; numTry < 1000; numTry++){
			testDistancesUsingRandom(false);
			testVelocityUsingRandom(false);
			testDurationUsingRandom(false);
		}
		
	}
	
	public static void testDistancesUsingRandom(boolean debug){
		int randomNumMiles = (int)(Math.random()*30000 + 50);
		int randomNumKm = (int)(Math.random()*45000 + 50 + randomNumMiles*2);
		
		if(debug){
			System.out.println(String.format("Testing Distance with parameters: %s,%s", randomNumMiles,randomNumKm));
		}
		
		testDistance(randomNumMiles,randomNumKm);
	}
	
	public static void testVelocityUsingRandom(boolean debug){
		int randomNumMilesPerHour = (int)(Math.random()*1000 + 10);
		int randomNumKmPerHour = (int)(Math.random()*1600 + 17 + randomNumMilesPerHour*2);
		int randomNumMeters = (int)(Math.random()*10000000 + 40);
		int randomNumSeconds = (int)(Math.random()*20000 + 20);
		
		if(debug){
			System.out.println(String.format("Testing Velocity with parameters: %s,%s,%s,%s", 
					randomNumMilesPerHour,randomNumKmPerHour,randomNumMeters,randomNumSeconds));
		}
		
		testVelocity(randomNumMilesPerHour,randomNumKmPerHour,randomNumMeters,randomNumSeconds);
	}
	
	public static void testDurationUsingRandom(boolean debug){
		int randomNumSeconds = (int)(Math.random()*400 + 20);
		int randomNumMinutes = (int)(Math.random()*55 + 2);
		int randomNumHours = (int)(Math.random()*80 + 2);
		
		int randomNumMiPerHour = (int)(Math.random()*1000 + 10);
		int randomNumMiles = (int)(Math.random()*12000 + 2);
		
		int randomNumKmPerHour = (int)(Math.random()*1600 + 10);
		int randomNumKm = (int)(Math.random()*12000 + 2);
		
		if(debug){
			System.out.println(String.format("Testing Duration with parameters: %s,%s,%s,%s,%s,%s,%s", 
					randomNumSeconds,randomNumMinutes,randomNumHours,randomNumMiPerHour,randomNumMiles,randomNumKmPerHour,randomNumKm));
		}
		
		testDuration(randomNumSeconds,randomNumMinutes,randomNumHours,randomNumMiPerHour,randomNumMiles,randomNumKmPerHour,randomNumKm);
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
		assertTrue(withinRange(mileDuration.getValue(),divideInts(numMiles,numMilesPerHour)*3600 ));
		
		Velocity kmph = Velocity.constructUsingKmPerHour(numKmPerHour);
		Distance kilometers = Distance.constructUsingMeters(numKm*1000);
		Duration kmDuration = Duration.constructUsingVelocityAndDistance(kmph, kilometers);
		assertTrue(withinRange(kmDuration.getValue(), divideInts(numKm,numKmPerHour)*3600 ));
		
		
		Duration testOneMinute = Duration.constructUsingSeconds(60);
		assertEquals(testOneMinute.toString(),"1 min");
		
		Duration testMinutes = Duration.constructUsingSeconds(numMinutes*60);
		assertEquals(testMinutes.toString(),numMinutes + " mins");
				
		Duration testOneHour = Duration.constructUsingSeconds(3600);
		assertEquals(testOneHour.toString(),"1 hr");
		
		Duration testOneHourOneMin = testOneHour.add(testOneMinute);
		assertEquals(testOneHourOneMin.toString(), "1 hr 1 min");
		
		Duration test59Mins = testOneHour.subtract(testOneMinute);
		assertEquals(test59Mins.toString(),"59 mins");
		
		Duration testOneHourAndMins = testOneHour.add(testMinutes);
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
		assertTrue(slowOne.getImperialText().endsWith(" mi/hr"));
		assertTrue(slowOne.toString().endsWith(" mi/hr"));
		assertTrue(withinRange(slowOne.getRoundedValue(),(slowMPH*1609.34)/3600));
		
		Velocity fastOne = Velocity.constructUsingKmPerHour(fastKmPH);
		assertTrue(fastOne.getMetricText().endsWith(" km/hr"));
		assertTrue(withinRange(fastOne.getValue(),divideInts((fastKmPH*1000),3600)));
		
		assertEquals(fastOne.compareTo(slowOne),1);
		assertEquals(slowOne.compareTo(fastOne),-1);
		
		Velocity otherVeloc = Velocity.constructUsingMetersAndSeconds(numMeters, numSeconds);
		assertTrue(withinRange(otherVeloc.getRoundedValue(),divideInts(numMeters,numSeconds)));

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
	
	public static double divideInts(int one, int two){
		double oneD = one;
		double twoD = two;
		return oneD/twoD;
	}
	
	public static boolean withinRange(double one, double two){
		return (Math.abs(one-two) <= 1);
	}

}
