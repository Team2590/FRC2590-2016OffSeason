package autoActions;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class FixIntake{

	private boolean done = false;
	
	/**
	 * Just puts the intake down
	 */
	public void downWithTheIntake() {
		Robot.arm.setMotorSpeed(-0.7);
		Timer.delay(.4);
		Robot.arm.setMotorSpeed(0);
		Robot.arm.setEnabled(true);
		done = true;
		
	}

	public boolean end() {
		// TODO Auto-generated method stub
		return done;
	}
}
