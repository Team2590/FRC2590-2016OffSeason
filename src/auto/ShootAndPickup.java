package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveTrajectory;
import autoActions.FixIntake;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;

/**
 * A lowbar autonomous which drops a ball
 * picks up the second , shoots that one, and picks up the first
 * @author Connor_Hofenbitzer
 *
 */
public class ShootAndPickup extends AutoModeTemplate{

	DriveTrajectory shootFirstBall , pickUpNext;
	FixIntake fix;
	
	public ShootAndPickup() {
		shootFirstBall = new DriveTrajectory("low_bar_to_left_batter_slow");
		pickUpNext = new DriveTrajectory("Connor_pick_up_next", true, 0);
		fix = new FixIntake();
	}
	
	@Override
	public void run() {
		Robot.intake.gimmeTehBall();
		fix.downWithTheIntake();
		Timer.delay(.6);
		
		shootFirstBall.startPath();
		waitTillDone(shootFirstBall::end, Vision.seesTarget(), () -> Robot.hood.setHood(14));
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

}
