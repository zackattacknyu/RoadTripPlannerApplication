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
		Distance myDist = Distance.constructUsingMeters(130*1609.34);
		
		Duration myDur = Duration.constructUsingSeconds(19000.54434);
		
		Velocity mySpeed = Velocity.constructUsingMiPerHour(65);
		
		assertEquals(myDist.getImperialText(),"130 mi");
		assertEquals(myDur.toString(),"34 hrs");
		assertEquals(myDur.getImperialText(),"65 mi/hr");
		
		assert myDur.getImperialText().isEmpty();
	}

}
