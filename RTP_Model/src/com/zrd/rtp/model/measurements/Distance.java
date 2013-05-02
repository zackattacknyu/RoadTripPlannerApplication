package com.zrd.rtp.model.measurements;


/**
 * This class represents a single distance measurement. 
 * 
 * It will construct the measurement using meters, as that is the value 
 * 		returned by Google. It will then convert that to either
 * 		km or miles depending on whether metric or imperial units are being used. 
 * 
 * Conversion facts used here:
 * 		1 mile = 1609.34 meters
 * 		1 km = 1000 meters
 * @author Zach DeStefano
 *
 */
public class Distance extends Measurement{


	
	protected Distance(double value) {
		super(value);
	}
	
	/**
	 * This takes in the number of meters from google and 
	 * 		returns the distance object for it. 
	 * @param numMeters		the double for the number of meters
	 * @return				the distance object for number of meters
	 */
	public static Distance constructUsingMeters(double numMeters){
		return new Distance(numMeters);
	}

	/**
	 * This gets the text of this distance using km as the unit
	 * @return		the text of the distance, with the unit km appended to the end
	 */
	public String getMetricText(){
		return String.valueOf((int)Math.round(value/1000)) + " km";
	}
	
	/**
	 * This gets the text of this distance using miles as the unit
	 * @return		the text of the distance, with the unit mi appended to the end
	 */
	public String getImperialText(){
		return String.valueOf((int)Math.round(value/1609.34)) + " mi";
	}

	@Override
	public Measurement add(Measurement val) {
		return new Distance(getValue() + val.getValue());
	}

	@Override
	public Measurement subtract(Measurement val) {
		return new Distance(getValue() - val.getValue());
	}

	
	

}
