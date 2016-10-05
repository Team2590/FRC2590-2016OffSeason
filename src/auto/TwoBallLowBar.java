package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveTrajectory;
import autoActions.FixIntake;
import autoActions.SetHood;
import autoActions.Turn;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;

public class TwoBallLowBar extends AutoModeTemplate{
	
	DriveTrajectory shootFirstBall , pickUpSecond , shootLast;
	Turn turnToShoot1 , turnToShoot2;
	SetHood hoodUp , hoodDown;
	FixIntake DropAndPickUp;
	
	public TwoBallLowBar() {
		
		//paths
		shootFirstBall = new DriveTrajectory("Connor_low_bar_to_left_batter_fast");
		pickUpSecond = new DriveTrajectory("Connor_pick_up_next", true, 0);
		shootLast = new DriveTrajectory("Connor_shoot_Last");
		
		//articulation
		DropAndPickUp = new FixIntake();
		hoodDown = new SetHood(2);
		hoodUp = new SetHood(17);
		
		//turns
		turnToShoot1 = new Turn();
		turnToShoot2 = new Turn();
	}
	
	@Override
	public void run() {
		
		//pick up the first ball
		DropAndPickUp.downWithTheIntake();
		Robot.intake.gimmeTehBall();
		Timer.delay(0.7);
		
		//get ready to rush
		Robot.intake.stopBoi();
		Robot.shooter.setRPM(5400);
		
		//go to the tower
		shootFirstBall.startPath();
		waitTillDone(shootFirstBall::end, Vision.seesTarget(), hoodUp::updateHood);
		
		//turn to target
		hoodUp.updateHood();
		turnToShoot1.startTurn(Vision.getTargetOffset());
    	waitTillDone(turnToShoot1::finished);

    	//shoot
		Robot.shooter.setFeederSpeed(1);
		Timer.delay(1);
		
		//get ready to go back under the lowBar
		Robot.intake.gimmeTehBall();
		Robot.shooter.setFeederSpeed(0);
		hoodDown.updateHood();
		Timer.delay(0.5);
		
		//go under the lowBar
		pickUpSecond.startPath();
		waitTillDone(pickUpSecond::end);
		Timer.delay(0.1);
		
		//go back through the lowbar
		shootLast.startPath();
		waitTillDone(shootLast::end, Vision.seesTarget(), hoodUp::updateHood);
		
		//turn to shoot
		hoodUp.updateHood();
		turnToShoot2.startTurn(Vision.getTargetOffset());
    	waitTillDone(turnToShoot2::finished);

    	//shoot
    	Robot.shooter.setFeederSpeed(1);
	}

	@Override
	public void cancel() {
		shootFirstBall.cancel();
		pickUpSecond.cancel();
		shootLast.cancel();
	}

}
