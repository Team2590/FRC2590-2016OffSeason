package autoActions;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import subsystems.Arm;
import subsystems.DriveTrain.DriveStates;

/**
 * <b>ARM</b> drops the arm , intended for begining of auto
 * @author Connor_Hofenbitzer
 * @see Arm.java
 */
public class FixIntake{

	private boolean done = false;
	private Arm inta;
	 
	public FixIntake(){
		inta = Arm.getInstance();
	}
	
	/**
	 * Just puts the intake down
	 */
	public void downWithTheIntake() {
		Robot.drivetrain.setState(DriveStates.LOCKED);
		inta.setMotorSpeed(-0.8);
		Timer.delay(.3);
		inta.setMotorSpeed(0);
		inta.setEnabled(true);
		done = true;
		
	}

	public boolean end() {
		// TODO Auto-generated method stub
		return done;
	}
}
