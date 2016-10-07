package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.FixIntake;
import subsystems.DriveTrain.DriveStates;

/**
 * <b>NULL</b> Sit there and put down the intake
 * @author Connor_Hofenbitzer
 *
 */
public class DoJackS extends AutoModeTemplate{

	private FixIntake intakeDown;
	
	public DoJackS(){
		intakeDown = new FixIntake();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		intakeDown.downWithTheIntake();
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		Robot.drivetrain.setState(DriveStates.TELEOP);
	}
	

}
