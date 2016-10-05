package autoActions;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class Turn {
	
	
	/**
	 * Turn to an angle
	 * @param angle : angle to turn to
	 */
	public void startTurn(double angle){
		Robot.drivetrain.setAuto(true);
    	while(!Robot.drivetrain.done())
    		Robot.drivetrain.turnToAngle(angle);
    	Timer.delay(.2);
    	Robot.drivetrain.stopMotors();
	}
	
	public boolean finished(){
		return Robot.drivetrain.done();
	}
	
}
