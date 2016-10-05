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

public class TwoBallLowBar extends AutoModeTemplate{
	
	DriveTrajectory shootFirstBall , pickUpSecond , shootLast;
	Turn turnToShoot1 , turnToShoot2;
	final static int HOODANGLE = 15;
	FixIntake DropAndPickUp;
	Shooter st;
	Hood hd;
	Intake in;
	
	public TwoBallLowBar() {
		
		//paths
		shootFirstBall = new DriveTrajectory("Connor_low_bar_to_left_batter_fast");
		pickUpSecond = new DriveTrajectory("Connor_pick_up_next", true, 0);
		shootLast = new DriveTrajectory("Connor_shoot_Last" , false , 0);
		
		//articulation
		DropAndPickUp = new FixIntake();
		
		//turns
		turnToShoot1 = new Turn();
		turnToShoot2 = new Turn();
		
		st = Shooter.getInstance();
		hd = Hood.getInstance();
		in = Intake.getInstance();
	}
	
	@Override
	public void run() {
		
		//pick up the first ball
		DropAndPickUp.downWithTheIntake();
		in.gimmeTehBall();
		Timer.delay(0.7);
		
		//get ready to rush
		in.stopBoi();
		st.setRPM(5400);
		
		//go to the tower
		shootFirstBall.startPath();
		waitTillDone( shootFirstBall::end, Vision.seesTarget(), () -> Robot.hood.setHood(HOODANGLE) );
		
		//turn to target
		hd.setHood(HOODANGLE);
		turnToShoot1.startTurn(Vision.getTargetOffset());
    	waitTillDone(turnToShoot1::finished);

    	//shoot
		st.setFeederSpeed(1);
		Timer.delay(1);
		
		//get ready to go back under the lowBar
		in.gimmeTehBall();
		st.setFeederSpeed(0);
		hd.setHood(2);
		Timer.delay(0.5);
		
		//go under the lowBar
		pickUpSecond.startPath();
		waitTillDone(pickUpSecond::end);
		Timer.delay(0.1);
		
		//go back through the lowbar
		shootLast.startPath();
		waitTillDone(shootLast::end, Vision.seesTarget(), () -> Robot.hood.setHood(HOODANGLE));
		
		//turn to shoot
		hd.setHood(HOODANGLE);
		turnToShoot2.startTurn(Vision.getTargetOffset());
    	waitTillDone(turnToShoot2::finished);

    	//shoot
    	st.setFeederSpeed(1);
	}

	@Override
	public void cancel() {
		shootFirstBall.cancel();
		pickUpSecond.cancel();
		shootLast.cancel();
	}

}
