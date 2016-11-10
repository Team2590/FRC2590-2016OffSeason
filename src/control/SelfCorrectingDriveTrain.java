package control;

import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

/**
 * <b>PROTOTYPE</b> : corrects the robot so that the robot drives completely straight if not trying to turn
 * <br>If defense is being played on our robot , this should give a bit of kickback
 * @author Connor_Hofenbitzer
 *
 */
public class SelfCorrectingDriveTrain implements RobotMap{

	//variables
	final double TOLERANCE = .1;
    boolean lastEnabled = false;
	boolean enabled = true; 
    
	//heading correcting PID
	PID turnCorrection;
    
	
	public SelfCorrectingDriveTrain() {
		
		//control system 
		turnCorrection = new PID(KP, KI, KD, false , 0.3);
		turnCorrection.set(0);
		
	}
	
	/**
	 * Start adjusting for drift , especially helpful for driving over defenses
	 * @param moveValue : the value for moving straight
	 * @param turnValue : turning value
	 */
	public void startCorrecting(double moveValue , double turnValue , boolean disabled) {
		
		//check if we are trying to turn
		enabled = (Math.abs(turnValue) <= TOLERANCE);
		
		//if we were just turning make sure that we know to make the new 0 ahead of you
		if(lastEnabled != enabled) {
			Robot.drivetrain.gyro.reset();
		}
		
		//if were not trying to turn then start adapting to a 0 heading
		if(enabled && !disabled) {
				Robot.drivetrain.driveTrain.arcadeDrive(moveValue, 
					turnCorrection.getOutput(Robot.drivetrain.gyro.getAngle()) );
			
		} else if(!enabled && !disabled) {
			//other wise we will allow them to turn
			Robot.drivetrain.driveTrain.arcadeDrive( moveValue , turnValue );
			
		} else if(disabled){ 
			//other wise we let the control system (auto usually) take control
		}
		
		//latching boolean
		lastEnabled = enabled;
		
	}
	
}
