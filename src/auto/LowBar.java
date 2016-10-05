package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveTrajectory;
import autoActions.FixIntake;
import autoActions.SetHood;
import autoActions.Turn;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;

public class LowBar extends AutoModeTemplate{
	
	static DriveTrajectory PickUpAndRun;
	static SetHood readyToShoot;
	static FixIntake dropBall;
	static Turn turnToTarget;
	
	static final int SHOTSPEED = 5000;
	static final int HOODANGLE = 12;
	
	public LowBar(){

		PickUpAndRun = new DriveTrajectory("low_bar_to_left_batter_slow");
		readyToShoot = new SetHood(HOODANGLE);
		dropBall = new FixIntake();
		turnToTarget = new Turn();

	}
	
	public void run(){
		
		//make sure everythings alright?
		Robot.drivetrain.setAuto(true);
		Robot.shooter.setAuto(false);
		Robot.hood.setHood(0);
		
		//fix all the stuffs 
		Robot.drivetrain.resetAllSensors();
		dropBall.downWithTheIntake();
		Robot.intake.gimmeTehBall();
		Timer.delay(1);
		
		//were done picking up the ball , get ready to drive
		Robot.shooter.setRPM(SHOTSPEED);
		Robot.intake.stopBoi();
		
		//start the follower
		PickUpAndRun.startPath();
		//we are waiting until the follower is finished , but if we see a tower , get the hood ready
		waitTillDone(PickUpAndRun::end , Vision.seesTarget() , readyToShoot::updateHood);
		Timer.delay(.1);
		Robot.drivetrain.stopMotors();
		
		//turn to target and make sure the hoods up
    	Robot.hood.setHood(12);
    	turnToTarget.startTurn(Vision.getTargetOffset());
    	waitTillDone(turnToTarget::finished);
    	
    	//when finished driving make sure the motors are stopped
    	Robot.drivetrain.stopMotors();
    	
    	//shoot
		Robot.shooter.setFeederSpeed(1);
		
	}

	@Override
	public void cancel() {
		Robot.shooter.setRPM(0);
		Robot.shooter.setAuto(true);
		Robot.intake.stopBoi();
		PickUpAndRun.cancel();
		Robot.drivetrain.setAuto(false);
	}
	
	
}
