package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveStraight;
import autoActions.FixIntake;
import edu.wpi.first.wpilibj.Timer;

public class GoOverDefenseNoShot extends AutoModeTemplate{

	static DriveStraight goOverDefense;
	static FixIntake intakeDown;
	
	public GoOverDefenseNoShot() {
		goOverDefense = new DriveStraight(-8,8,8);
		intakeDown = new FixIntake();	
	}
	

	@Override
	public void run() {
		Robot.drivetrain.resetAllSensors();
		intakeDown.downWithTheIntake();
		Timer.delay(1);
		goOverDefense.run();
	}

	@Override
	public void cancel() {
		goOverDefense.cancel();
	}

}
