package autoActions;

import org.usfirst.frc.team2590.robot.Robot;

public class SetHood{
	
	private int setPoint;
	
	public SetHood(int setPoint){
		this.setPoint = setPoint;
	}
	
	/**
	 * Set the hood to a given angle
	 */
	public void updateHood() {
		Robot.hood.setHood(setPoint);
	}

}
