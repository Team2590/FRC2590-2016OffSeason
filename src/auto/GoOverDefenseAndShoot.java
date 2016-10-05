package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveStraight;
import autoActions.FixIntake;
import autoActions.Turn;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;

public class GoOverDefenseAndShoot extends AutoModeTemplate{

	static int position , shotRPM, hoodA , currentRotation1 , currentRotation2;
	static Turn turnAfterOver , turnWhenCloser , Noscope , turnToZ;
	static DriveStraight overDefense , toTower ;
	static FixIntake fixer;

	
	public GoOverDefenseAndShoot() {
		
		//constants
		shotRPM = 5400;
		hoodA = 12;
		
		//driving
		overDefense = new DriveStraight(-8,8,8);
		toTower = new DriveStraight(4,6,6);
		fixer = new FixIntake();
		
		//turning
		turnAfterOver = new Turn();
		turnWhenCloser = new Turn();
		Noscope = new Turn();
		turnToZ = new Turn();
	}
	

	@Override
	public void run() {
		
		//put the intake down
		Robot.shooter.setAuto(false);
		Robot.drivetrain.resetAllSensors();
		fixer.downWithTheIntake();
		Timer.delay(.7);
		
		//go over the defense
		overDefense.run();
		Robot.shooter.setRPM(shotRPM);
		Robot.hood.setHood(hoodA);
		waitTillDone(overDefense::done);
		
		//run the courtyard part of the auto
		afterD();
		
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}
	private void afterD(){
			//turn to tower
			Noscope.startTurn(180);
			waitTillDone(Robot.drivetrain::done);
			Timer.delay(.1);
			
			//look at the tower
			turnAfterOver.startTurn(Vision.getTargetOffset());
			waitTillDone(Robot.drivetrain::done);
			currentRotation1 = (int)Robot.drivetrain.gyro.getAngle();
			
			//drive to tower
			toTower.run();
			waitTillDone(toTower::done);
			
			//turn to tower again
			turnWhenCloser.startTurn(Vision.getTargetOffset());
			waitTillDone(Robot.drivetrain::done);
			currentRotation2 = (int)Robot.drivetrain.gyro.getAngle();
			
			Robot.shooter.setFeederSpeed(1);
			Timer.delay(1);
			
			turnToZ.startTurn(currentRotation1+currentRotation2);
	}
}
