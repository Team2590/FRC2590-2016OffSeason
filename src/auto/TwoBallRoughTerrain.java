package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveStraight;
import autoActions.FixIntake;
import autoActions.Turn;
import control.ShootingParameters;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;

public class TwoBallRoughTerrain extends AutoModeTemplate {

	DriveStraight firstShot , pickUp , secondShot;
	ShootingParameters shootTheBall;
	FixIntake dropIntake;
	Turn turnToTower1 , turnToTower2 , fixRobot , fixRobot2;
	double placeHolder1;
	
	public TwoBallRoughTerrain() {
		
		shootTheBall = new ShootingParameters(14, 5400, false);

		secondShot = new DriveStraight(14, 8, 8);
		firstShot = new DriveStraight(14, 8, 8);
		pickUp = new DriveStraight(-16, 8, 8);
				
		dropIntake = new FixIntake();

		turnToTower1 = new Turn();
		turnToTower2 = new Turn();
		fixRobot = new Turn();
		
		placeHolder1 = 0;
		
	}
	
	@Override
	public void run() {
		
		//get ready to go forward
		dropIntake.downWithTheIntake();
		
		//go for the shot
		firstShot.run();
		shootTheBall.start();
		waitTillDone(firstShot::done);
		Timer.delay(.2);
		
		//turn to target
		turnToTower1.startTurn(Vision.getTargetOffset());
		waitTillDone(turnToTower1::finished);
		Timer.delay(.2);
		
		//fire
		shootTheBall.startRollers();
		Timer.delay(.5);
		
		//get Ready to go back
		shootTheBall.stopRoller();
		shootTheBall.setAuto(false);
		Robot.intake.gimmeTehBall();
		
		//start to pick up the next ball
		pickUp.run();
		waitTillDone(pickUp::done);
		Timer.delay(.1);
		
		//turn back to 0
		placeHolder1 = Robot.drivetrain.gyro.getAngle();
		fixRobot.startTurn(-placeHolder1);
		
		//go to shoot the second ball
		secondShot.run();
		waitTillDone(secondShot::done);
		
		//turn to tower again
		turnToTower2.startTurn(Vision.getTargetOffset());
		waitTillDone(turnToTower2::finished);

		//shoot the second ball
		shootTheBall.startRollers();
	}

	@Override
	public void cancel() {
		
	}

}
