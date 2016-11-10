package autoActions;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import subsystems.Arm;

/**
 * <b>ARM</b> drops the arm , intended for begining of auto
 * @author Connor_Hofenbitzer
 * @see Arm.java
 */
public class FixIntake{

	private boolean done = false;
	 
	public FixIntake(){
	}
	
	/**
	 * Just puts the intake down
	 */
	public void downWithTheIntake() {		
		//drop the arm
		Robot.arm.setMotorSpeed(-0.65);
		Timer.delay(.5);
		//stop dropping
		Robot.arm.setMotorSpeed(0);
		Robot.arm.setSetpoint(0);
		done = true;
	}

	public boolean end() {
		// TODO Auto-generated method stub
		return done;
	}
}
