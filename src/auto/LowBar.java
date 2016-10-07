package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveTrajectory;
import autoActions.FixIntake;
import autoActions.Turn;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;
import subsystems.DriveTrain.DriveStates;
import subsystems.Hood;
import subsystems.Intake;
import subsystems.Shooter;

/**
 * <b>LOWBAR</b> Drives through the lowbar and takes the shot
 * @author Connor_Hofenbitzer
 *
 */
public class LowBar extends AutoModeTemplate{
	
	static DriveTrajectory PickUpAndRun;
	static FixIntake dropBall;
	static Turn turnToTarget;
		   Shooter st;
		   Intake in;
		   Hood hd;
		   
	static final int SHOTSPEED = 5000;
	static final int HOODANGLE = 12;
	
	public LowBar(){

		PickUpAndRun = new DriveTrajectory("low_bar_to_left_batter_slow");
		dropBall = new FixIntake();
		turnToTarget = new Turn();
		
		st = Shooter.getInstance();
		in = Intake.getInstance();
		hd = Hood.getInstance();
		st.setAuto(false);
	}
	
	public void run(){
		
		//make sure everythings alright?
		Robot.drivetrain.setState(DriveStates.LOCKED);
		st.setAuto(false);
		hd.setHood(0);
		
		//fix all the stuffs 
		dropBall.downWithTheIntake();
		in.gimmeTehBall();
		Timer.delay(1);
		
		//were done picking up the ball , get ready to drive
		st.setRPM(SHOTSPEED);
		in.stopBoi();
		
		//start the follower
		PickUpAndRun.startPath();
		//we are waiting until the follower is finished , but if we see a tower , get the hood ready
		waitTillDone(PickUpAndRun::end , Vision.seesTarget() , () -> Robot.hood.setHood(HOODANGLE));
		Timer.delay(.1);
		Robot.drivetrain.stopMotors();
		
		//turn to target and make sure the hoods up
    	hd.setHood(12);
    	turnToTarget.startTurn(Vision.getTargetOffset());
    	waitTillDone(turnToTarget::finished);
    	
    	//when finished driving make sure the motors are stopped
    	Robot.drivetrain.stopMotors();
    	
    	//shoot
		st.setFeederSpeed(1);
		
	}

	@Override
	public void cancel() {
		Robot.shooter.setRPM(0);
		Robot.shooter.setAuto(true);
		Robot.intake.stopBoi();
		PickUpAndRun.cancel();
		Robot.drivetrain.setState(DriveStates.TELEOP);
	}
	
	
}
