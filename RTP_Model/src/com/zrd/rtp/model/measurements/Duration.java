package com.zrd.rtp.model.measurements;

/**
 * This class is meant to represent a single duration. 
 * 
 * It will construct the duration using seconds as that is the value
 * 		returned by google. 
 * 
 * It will return a text of the duration using the form x hours y mins
 * 		or y mins if less than one hour. 
 * 
 * The word days is purposefully NOT included as that can be ambiguous. 
 * 		Some mapping programs mean 10 or 12 hours for a day and not 24 hours.
 * 		If a duration is longer than 24 hours, it will just display as that. 
 *  
 * @author Zach DeStefano
 *
 */
public class Duration extends Measurement {
	
	
	protected Duration(double value) {
		super(value);
	}

	/**
	 * This constructs a duration instance using seconds
	 * @param numSeconds	number of seconds for the instance
	 * @return				the corresponding duration
	 */
	public static Duration constructUsingSeconds(double numSeconds){
		return new Duration(numSeconds);
	}

	/**
	 * This constructs a duration using the velocity and distance. This is used
	 * 		by the accurate time estimator to find out the duration of different
	 * 		legs given a revised speed. 
	 * @param velocity		the velocity object representing the velocity being used
	 * @param distance		the distance object representing the distance traveled
	 * @return			the duration that the leg took given velocity and distance
	 */
	public static Duration constructUsingVelocityAndDistance(Velocity velocity,Distance distance){
		return new Duration(distance.getValue()/velocity.getValue());
	}
	
	/**
	 * This contructs the duration string, which will have either one of the following forms:
	 * 		"x mins" or 
	 * 		"x hrs y mins".
	 * 
	 * Days will NOT be included for reasons explained above. 
	 * @return		duration text string
	 */
	@Override
	public String toString(){
		int numMinutes = (int) Math.round(value/60);
		int displayNumHours = numMinutes/60;
		int displayNumMinutes = numMinutes%60;
				
		String hoursPart = String.format(
				getFormatString(displayNumHours,"hr"), displayNumHours);
		String minutesPart = String.format(
				getFormatString(displayNumMinutes,"min"), displayNumMinutes);
		
		if(hoursPart.length() > 0){
			
			if(minutesPart.length() > 0){
				return hoursPart + " " + minutesPart;
			}else{
				return hoursPart;
			}
			
		}else{
			return minutesPart;
		}
		
	}
	
	@Override
	public Duration add(Measurement val) {
		return (Duration)super.add(val);
	}

	@Override
	public Duration subtract(Measurement val) {
		return (Duration)super.subtract(val);
	}

	private static String getFormatString(int number,String prefix){
		if(number < 1){
			return "";
		}else if(number < 2){
			return "%s " + prefix;
		}else{
			return "%s " + prefix + "s";
		}
	}

	/**
	 * This method is NOT SUPPORTED. Use toString() to get the text 
	 * 		for the duration. 
	 * @return	Throws Exception
	 */
	@Override
	public String getMetricText() {
		throw new UnsupportedOperationException("Durations do not vary by metric or imperial. Use toString to get the text.");
	}
	
	/**
	 * This method is NOT SUPPORTED. Use toString() to get the text 
	 * 		for the duration. 
	 * @return	Throws Exception
	 */
	@Override
	public String getImperialText() {
		throw new UnsupportedOperationException("Durations do not vary by metric or imperial. Use toString to get the text.");
	}
	
	@Override
	protected Measurement constructUsingValue(double value) {
		return constructUsingSeconds(value);
	}



}
