package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveStraight;
import autoActions.FixIntake;
import control.ShootingParameters;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;

public class ChivalDeAuto extends AutoModeTemplate{

	private static DriveStraight driveToChival , driveOverChival , driveToTower;
	private static FixIntake intakeDown; 
	private static ShootingParameters takeTheShot;
	
	public ChivalDeAuto() {

		//the paths happen in this order
		driveToChival = new DriveStraight(-5, 6, 6);
		driveOverChival = new DriveStraight(-3, 3, 3);
		driveToTower = new DriveStraight(-4, 6, 6);
		
		takeTheShot = new ShootingParameters(12, 5400, false);
		intakeDown = new FixIntake();
	}
	

	@Override
	public void run() {
		
		Robot.drivetrain.setAuto(true);
		
		//drive to chival
		Robot.drivetrain.resetAllSensors();
		driveToChival.run();
		waitTillDone(driveToChival::done);
		Timer.delay(0.2);
		
		//put down chival
		intakeDown.downWithTheIntake();
		Timer.delay(0.8);
		
		//drive over chival
		driveOverChival.run();
		Robot.shooter.setAuto(false);
		takeTheShot.start();
		waitTillDone(driveOverChival::done);
		Timer.delay(0.2);
		
		//drive to the tower
		driveToTower.run();
		waitTillDone(driveToTower::done);
		
		//180 turn
		Robot.drivetrain.setAuto(true);
		while(!Robot.drivetrain.done())
		Robot.drivetrain.turnToAngle(180);
		
		//gotta be on target or bust m9
		Robot.drivetrain.setAuto(true);
		while(!Robot.drivetrain.done())
		Robot.drivetrain.turnToAngle(Vision.getTargetOffset());
		
		//360 no scope m8
		takeTheShot.startRollers();
		
	}

	@Override
	public void cancel() {
		driveToChival.cancel();
		driveOverChival.cancel();
		driveToTower.cancel();
		takeTheShot.cancel();
		Robot.intake.stopBoi();
		Robot.drivetrain.setAuto(false);
	}

}
