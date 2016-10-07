package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveStraight;
import autoActions.FixIntake;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;
import subsystems.DriveTrain.DriveStates;
import subsystems.Shooter;

/**
 * <b>CHIVAL</b> Drive over the chival, Turns around, looks for the target,
 * drives to the tower,
 * and shoots
 * @author Connor_Hofenbitzer
 *
 */
public class ChivalDeAuto extends AutoModeTemplate{

	DriveStraight driveToChival , driveOverChival , driveToTower;
	FixIntake intakeDown; 
	Shooter st;
	
	public ChivalDeAuto() {

		//the paths happen in this order
		driveToChival = new DriveStraight(-5, 6, 6);
		driveOverChival = new DriveStraight(-3, 3, 3);
		driveToTower = new DriveStraight(-4, 6, 6);
		
		intakeDown = new FixIntake();
		st = Shooter.getInstance();
		st.setAuto(false);
	}
	

	@Override
	public void run() {
		
		
		//drive to chival
		driveToChival.run();
		waitTillDone(driveToChival::done);
		Timer.delay(0.2);
		
		//put down chival
		intakeDown.downWithTheIntake();
		Timer.delay(0.8);
		
		//drive over chival
		driveOverChival.run();
		st.setAuto(false);
		st.setRPM(5400);
		waitTillDone(driveOverChival::done);
		Timer.delay(0.2);
		
		//drive to the tower
		driveToTower.run();
		waitTillDone(driveToTower::done);
		
		//180 turn
		while(!Robot.drivetrain.done())
		Robot.drivetrain.turnToAngle(180);
		
		//gotta be on target or bust m9
		while(!Robot.drivetrain.done())
		Robot.drivetrain.turnToAngle(Vision.getTargetOffset());
		
		//360 no scope m8
		st.setFeederSpeed(1);
	}

	@Override
	public void cancel() {
		driveToChival.cancel();
		driveOverChival.cancel();
		driveToTower.cancel();
		Robot.intake.stopBoi();
		Robot.drivetrain.setState(DriveStates.TELEOP);
	}

}
