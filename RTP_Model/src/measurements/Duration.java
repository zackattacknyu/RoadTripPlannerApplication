package measurements;

public class Duration implements Measurement {
	
	//value is in seconds
	private double value;
	
	private Duration(double numSeconds){
		value = numSeconds;
	}
	
	public static Duration calculateFromSeconds(int numSeconds){
		return new Duration(numSeconds);
	}
	
	public static Duration calculateFromSeconds(double numSeconds){
		return new Duration(numSeconds);
	}
	
	public static Duration calculateFromVelocityAndDistance(Velocity velocity,Distance distance){
		return new Duration(distance.getInternalValue()/velocity.getInternalValue());
	}
	
	@Override
	public String getImperialUnitsText() {
		return getText();
	}

	@Override
	public String getMetricUnitsText() {
		return getText();
	}
	
	public String getText(){
		int numMinutes = (int) Math.round(value/60);
		int displayNumHours = numMinutes/60;
		int displayNumMinutes = numMinutes%60;
				
		String hoursPart = String.format(
				getFormatString(displayNumHours,"hr"), displayNumHours);
		String minutesPart = String.format(
				getFormatString(displayNumMinutes,"min"), displayNumMinutes);
		
		if(hoursPart.length() > 0){
			return hoursPart + " " + minutesPart;
		}else{
			return minutesPart;
		}
		
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

	@Override
	public double getInternalValue() {
		return value;
	}

}
