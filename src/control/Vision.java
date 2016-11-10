package control;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * <b>COMMS</b> gets vision data from the dashboard
 * @author Connor_Hofenbitzer
 *
 */
public class Vision {
	
	static double offset = -2; //closer to 0 or greater than 0 = right
	static double FOV = 60;
	

	/**
	 * get the offset to the target
	 * @return value from 45ish to -45ish
	 */
	public static double getTargetOffset() {
		//subtract (X - center pixel) * (3.33) 
		//		return  ( (table.getNumber("centerX",0) - 100) * (pixels / FOV) ) + offset; //35.25

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
		//HEY REMEMBER TO REINTERPOLATE THIS DATA
		return x;
	}
	
}
