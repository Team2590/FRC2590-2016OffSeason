package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveStraight;
import autoActions.DriveTrajectory;
import autoActions.FixIntake;
import autoActions.Turn;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;

public class LowBar extends AutoModeTemplate{

	DriveTrajectory back;
	DriveTrajectory go;
	DriveStraight forward1 , forward2;
	Turn turnToTarget;
    FixIntake fix;
		
    @Override
	public void init() {
		go = new DriveTrajectory("low_bar_to_left_batter_slow", true);
		forward1 = new DriveStraight(19, 6, 6);
		forward2 = new DriveStraight(-4, 6, 6);
		turnToTarget = new Turn();
		fix = new FixIntake();		
	}
	
	@Override
	public void run() {
		//fix all the stuffs
		//Robot.intake.gimmeTehBall();
		Robot.hood.setHood(0);
		fix.downWithTheIntake();
			
		//start the follower and fix everything
		go.startPath();
		while(!go.end()) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		Robot.shooter.setRPM(5400);
		Robot.drivetrain.stopMotors();
	
		//turnToTarget.startTurn(45);

		Timer.delay(.5);

		//set The hood once over the lowbar
		Robot.hood.setHood(20);
			
		//turn to target
		turnToTarget.startTurn(Vision.getTargetOffset());
		while(!Robot.drivetrain.done()) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Timer.delay(1);

		
		//double check this ish!
		turnToTarget.startTurn(Vision.getTargetOffset());
		while(!Robot.drivetrain.done()) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Timer.delay(.5);

		//fire
		Robot.shooter.setFeederSpeed(1);
		
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		go.cancel();
		Robot.shooter.setRPM(0);
		Robot.drivetrain.forceTeleop();
		Robot.shooter.setFeederSpeed(0);
	}
	
}
