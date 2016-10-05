package control;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *Get the vision stuff from the dashboard
 */
public class Vision {
	
	static double offset = -4; //Decrease if missing right

	/**
	 * get the offset to the target
	 * @return value from 45ish to -45ish
	 */
	public static double getTargetOffset(){
		return SmartDashboard.getNumber("Target Center X", 0) * 31 + offset; //35.25
	}
	
	/**
	 * How far away are we from a target
	 * @return tower distance
	 */
	public static double getDistance(){
		return SmartDashboard.getNumber("Distance to Target",15);
	}
	
	/**
	 * Do we see a target?
	 * @return target found?
	 */
	public static boolean seesTarget(){
		return getTargetOffset() != offset;
	}
	
	/**
	 * Hood angle based off of tower distance
	 * @return hood angle
	 */
	public static double getHoodAngle(){
		double x = SmartDashboard.getNumber("Distance to Target", 7);
		//Enterpolated data
		if (x <= 8.3){
			return + .56108188 * Math.pow(x, 3) - 12.764580505 * Math.pow(x, 2) + 99.21279705 * x - 236.87571848;
		} else if(x < 2){
			return 29; // Assume outer works
		} else {
			return - .06715505 * Math.pow(x, 3) + 1.52593244 * Math.pow(x, 2) - 8.95116441 * x + 31.707057248;
		}
	}
	
}
