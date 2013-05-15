package com.zrd.rtp.model.data;

import java.util.Locale;

/**
 * This is an abstract class representing a single measurement. 
 * @author Zach DeStefano
 *
 */
public abstract class Measurement implements Comparable<Measurement>{
	
	/**
	 * This will add the current measurement to the one passed in and 
	 * 		return the result as a third measurement. 
	 * @param val		the measurement to add to the current one
	 * @return			the resultant measurement
	 */
	public Measurement add(Measurement val){
		return constructUsingValue(getValue() + val.getValue());
	}
	
	/**
	 * This will subtract the passed in measurement from the current measurement and 
	 * 		return the result as a third measurement. 
	 * @param val		the measurement to subtract from the current one
	 * @return			the resultant measurement
	 */
	public Measurement subtract(Measurement val){
		return constructUsingValue(getValue() - val.getValue());
	}
	
	/**
	 * This returns the text in the default metric unit
	 * @return		string in default metric unit
	 */
	public abstract String getMetricText();
	
	protected abstract Measurement constructUsingValue(double value);
	
	/**
	 * This returns the text in the default imperial unit
	 * @return		string in the default imperial unit
	 */
	public abstract String getImperialText();
	
	public abstract int getImperialValue();
	public abstract int getMetricValue();
	
	protected final double value;
	
	protected Measurement(double value){
		this.value = value;
	}
	
	/**
	 * This gets the measurement value as a double
	 * @return	the double for the measurement value
	 */
	public double getValue(){
		return value;
	}
	
	/**
	 * Does the same as getValue() except it is rounded to the nearest integer
	 * @return		the value rounded to the nearest integer
	 */
	public int getRoundedValue(){
		return (int)Math.round(value);
	}

	/**
	 * This compares measurements together by finding the difference
	 * 		between the values. If the difference is between -1 and 1, 
	 * 		they are considered equal. 
	 */
	@Override
	public int compareTo(Measurement o) {
		double difference = getRoundedValue() - o.getRoundedValue();
		if(difference < -1){
			return -1;
		}else if(difference > 1){
			return 1;
		}else{
			return 0;
		}
	}
	
	/**
	 * This sees if two values are equal by seeing if compareTo results
	 * 		in 0. For information on that logic, look at compareTo. 
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Measurement){
			return (compareTo((Measurement)obj) == 0);
		}else{
			return false;
		}
	}

	/**
	 * This finds the default locale and if it is US, then it calls getImperialText().
	 * 		Otherwise, it calls getMetricText().
	 */
	@Override
	public String toString() {
		if(Locale.getDefault().equals(Locale.US)){
			return getImperialText();
		}
		else{
			return getMetricText();
		}
	}

}
