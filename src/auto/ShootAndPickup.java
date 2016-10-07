package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveTrajectory;
import autoActions.FixIntake;
import autoActions.Turn;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;
import subsystems.Hood;
import subsystems.Intake;
import subsystems.Shooter;

/**
 * <b>LOWBAR</b> autonomous which drops a ball
 * picks up the second , shoots that one, and picks up the first
 * @author Connor_Hofenbitzer
 *
 */
public class ShootAndPickup extends AutoModeTemplate{

	DriveTrajectory shootFirstBall , pickUpNext;
	Turn turnToTarget;
	FixIntake fix;
	Shooter st;	
	Intake in;
	Hood hd;
	
	public ShootAndPickup() {
		shootFirstBall = new DriveTrajectory("low_bar_to_left_batter_drop");
		pickUpNext = new DriveTrajectory("Connor_pick_up_next", true, 0);
		turnToTarget = new Turn();
		fix = new FixIntake();

		hd = Hood.getInstance();
		in = Intake.getInstance();
		st = Shooter.getInstance();
		Robot.shooter.setAuto(false);
	}
	
	@Override
	public void run() {
		in.gimmeTehBall();
		fix.downWithTheIntake();
		st.setRPM(5400);
		Timer.delay(.6);
		
		shootFirstBall.startPath();
		waitTillDone(shootFirstBall::end, Vision.seesTarget(), () -> hd.setHood(19));
		hd.setHood(19);
		turnToTarget.startTurn(Vision.getTargetOffset());
		st.setFeederSpeed(1);
		Timer.delay(.5);
		
		st.setFeederSpeed(0);
		hd.setHood(0);
		st.setRPM(0);
		
		pickUpNext.startPath();
		waitTillDone(shootFirstBall::end);

	}

	@Override
	public void cancel() {
		shootFirstBall.cancel();
		pickUpNext.cancel();
	}

}
