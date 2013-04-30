package temp_tests;

import measurements.Duration;



public class DurationTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Duration one = Duration.calculateFromSeconds(2000);
		Duration two = Duration.calculateFromSeconds(4000);
		System.out.println(one.getImperialUnitsText());
		System.out.println(two.getMetricUnitsText());

	}

}
