package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveStraight;
import autoActions.FixIntake;
import autoActions.Turn;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;
import subsystems.Shooter;

/**
 * <b>TERRAIN</b> takes two shots going over rough terrain
 * @author Connor_Hofenbitzer
 *
 */
public class TwoBallRoughTerrain extends AutoModeTemplate {
	
	Turn turnToTower1 , turnToTower2 , fixRobot , fixRobot2;
	DriveStraight firstShot , pickUp , secondShot;
	FixIntake dropIntake;
	double placeHolder1;
	Shooter st;
	
	public TwoBallRoughTerrain() {
		

		secondShot = new DriveStraight(14, 8, 8);
		firstShot = new DriveStraight(14, 8, 8);
		pickUp = new DriveStraight(-16, 8, 8);
				
		dropIntake = new FixIntake();

		turnToTower1 = new Turn();
		turnToTower2 = new Turn();
		fixRobot = new Turn();
		
		st = Shooter.getInstance();
		placeHolder1 = 0;
		st.setAuto(false);
	}
	
	@Override
	public void run() {
		
		//get ready to go forward
		dropIntake.downWithTheIntake();
		
		//go for the shot
		firstShot.run();
		st.setRPM(5400);
		waitTillDone(firstShot::done);
		Timer.delay(.2);
		
		//turn to target
		turnToTower1.startTurn(Vision.getTargetOffset());
		waitTillDone(turnToTower1::finished);
		Timer.delay(.2);
		
		//fire
		st.setFeederSpeed(1);
		Timer.delay(.5);
		
		//get Ready to go back
		st.setFeederSpeed(0);
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
		st.setFeederSpeed(1);

	}

	@Override
	public void cancel() {
		
	}

}
