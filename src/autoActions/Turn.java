package autoActions;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import subsystems.DriveTrain;
import subsystems.DriveTrain.DriveStates;

/**
 * <b>DRIVETRAIN</b> Turns to the desired angle
 * @author Connor_Hofenbitzer
 * @see DriveTrain.java
 */
public class Turn {
	
	
	/**
	 * Turn to an angle
	 * @param angle : angle to turn to
	 */
	public void startTurn(double angle) {
		Robot.drivetrain.setState(DriveStates.TURNING);
    	while(!Robot.drivetrain.done())
    		Robot.drivetrain.turnToAngle(angle);
    	Timer.delay(.2);
    	Robot.drivetrain.stopMotors();
    	
	}
	
	public boolean finished(){
		return Robot.drivetrain.done();
	}
	
}
