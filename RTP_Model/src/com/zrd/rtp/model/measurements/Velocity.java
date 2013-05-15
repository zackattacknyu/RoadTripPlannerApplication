package com.zrd.rtp.model.measurements;

/**
 * This class is meant to be an object that represents a single velocity. 
 * 		It will be mainly used for the accurate time estimator. 
 * 
 * It will construct a velocity from either miles per hour, km per hour, or meters and seconds. 
 * 	For each leg of a journey, google will return the distance value in meters
 * 		and the time value in seconds, hence the basis being meters and seconds.
 * 
 *  To support both english and imperial units, it can return the text of the speed
 *  	in either miles per hour or kilometers per hour. 
 *  
 *  The following facts are used for the conversion: 
 *  	'1 mile = 1609.34 meters',
 *  	'1 km = 1000 meters', and
 *  	'1 hour = 3600 seconds'
 *  
 * @author Zach DeStefano
 *
 */
public class Velocity extends Measurement{

	private double numMeters;
	private double numHours;
	
	private Velocity(double numMeters, double numSeconds){
		super(numMeters/numSeconds);
		this.numMeters = numMeters;
		this.numHours = numSeconds/3600;
		
	}
	
	/**
	 * This gets the text of the velocity using km/hour as the unit
	 * @return		text of the velocity with km/hr at the end
	 */
	public String getMetricText(){
		return String.valueOf(getMetricValue()) + " km/hr";
	}
	
	/**
	 * This gets the text of the velocity using mi/hr as the unit
	 * @return		text of the velocity with mi/hr at the end
	 */
	public String getImperialText(){
		return String.valueOf(getImperialValue()) + " mi/hr";
	}
	
	@Override
	public int getImperialValue() {
		double numMiles = numMeters/1609.34;
		return (int)(numMiles/numHours);
	}

	@Override
	public int getMetricValue() {
		double numKm = numMeters/1000;
		return (int)(numKm/numHours);
	}
	
	/**
	 * This constructs a new velocity object after taking in the 
	 * 		number of miles per hour
	 * @param miPerHour		number of miles per hour
	 * @return				velocity object for velocity inputted
	 */
	public static Velocity constructUsingMiPerHour(double miPerHour){
		return new Velocity(miPerHour*1609.34,3600);
	}
	
	/**
	 * This constructs a new velocity object after taking in the 
	 * 		number of miles per hour
	 * @param kmPerHour		number of km per hour
	 * @return				velocity object for velocity inputted
	 */
	public static Velocity constructUsingKmPerHour(double kmPerHour){
		return new Velocity(kmPerHour*1000,3600);
	}
	
	/**
	 * This constructs a new velocity object after taking in the number of meters
	 * 		and the number of seconds for a particular leg. The input is this 
	 * 		way due to how the values are given by Google Directions API. 
	 * @param numMeters			the number of meters in a leg
	 * @param numSeconds		the number of seconds in a leg
	 * @return			velocity object representing the average speed during the leg
	 */
	public static Velocity constructUsingMetersAndSeconds(double numMeters, double numSeconds){
		return new Velocity(numMeters,numSeconds);
	}

	
	/**
	 * This method is NOT SUPPORTED. There is no use in the application for adding velocities
	 * 		together. 
	 * @param val		NOT SUPPORTED
	 * @return			Nothing. Exception will be thrown.
	 */
	@Override
	public Measurement add(Measurement val) {
		throw new UnsupportedOperationException("This application does not support adding velocities");
	}

	/**
	 * This method is NOT SUPPORTED. There is no use in the application for subtracting velocities
	 * 		together. 
	 * @param val		NOT SUPPORTED
	 * @return			Nothing. Exception will be thrown.
	 */
	@Override
	public Measurement subtract(Measurement val) {
		throw new UnsupportedOperationException("This application does not support subtracting velocities");
	}

	@Override
	protected Measurement constructUsingValue(double value) {
		throw new UnsupportedOperationException("This application does not support constructing velocities using meters per second");
	}

	
	
	


}
