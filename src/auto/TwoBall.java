package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveTrajectory;
import autoActions.FixIntake;
import autoActions.Turn;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;

public class TwoBall extends AutoModeTemplate {

	//drive Trajectories
	DriveTrajectory goAgain;
	DriveTrajectory back;
	DriveTrajectory go;
	
	//other commands
	Turn turnToTarget;
	FixIntake fix;


	
	@Override
	public void init() {
		
		//drive trajectories
		go = new DriveTrajectory("name", true);
		back = new DriveTrajectory("backNameHere", true, -Math.PI, false);
		goAgain = new DriveTrajectory("forwardAgain", false, -Math.PI , false);
		
		//other commands
		fix = new FixIntake();
		turnToTarget = new Turn();
		Robot.shooter.setAuto(false);
		
	}

	@Override
	public void run() {
		
		//intake the first ball
		fix.downWithTheIntake();
		Robot.intake.gimmeTehBall();
		Timer.delay(1);
		
		//start driving to the tower
		go.startPath();
		waitTillDone(go.end() , 
					 Vision.seesTarget() , 
					 () -> Robot.hood.setHood(20) , 
					 () -> Robot.shooter.setRPM(5800) );
		
		//start the shooter
		Robot.intake.stopBoi();
		Robot.hood.setHood(20);
		Robot.shooter.setRPM(5800);
		
		//turn to target
		turnToTarget.startTurn(Vision.getTargetOffset());
		waitTillDone(Robot.drivetrain.done());
		Timer.delay(.1);
		
		//double check
		turnToTarget.startTurn(Vision.getTargetOffset());
		waitTillDone(Robot.drivetrain.done());
		Timer.delay(.1);
		
		//shoot
		Robot.shooter.setFeederSpeed(1);
		Timer.delay(.5);
		
		//stop shooting
		Robot.shooter.setFeederSpeed(0);
		Robot.hood.setHood(0);
		
		//start the intake
		Robot.intake.gimmeTehBall();
		
		//go back
		back.startPath();
		waitTillDone(back.end());
		Timer.delay(.1);
		
		goAgain.startPath();
		waitTillDone(goAgain.end() , 
					 Vision.seesTarget() , 
					 () -> Robot.hood.setHood(20));
		
		Robot.hood.setHood(20);
		Robot.intake.stopBoi();
		
		turnToTarget.startTurn(Vision.getTargetOffset());
		waitTillDone(Robot.drivetrain.done());
		Timer.delay(.1);
		
		turnToTarget.startTurn(Vision.getTargetOffset());
		waitTillDone(Robot.drivetrain.done());
		Timer.delay(.1);
		
		Robot.shooter.setFeederSpeed(1);
		Timer.delay(.1);
		
		Robot.shooter.setFeederSpeed(0);
		Robot.hood.setHood(0);
	}

	@Override
	public void cancel() {
		
		//handle shooter
		Robot.hood.setHood(0);
		Robot.shooter.setRPM(0);
		Robot.shooter.setFeederSpeed(0);
				
		//handle driveTrain
		Robot.drivetrain.forceTeleop();
				
		//stop paths
		go.cancel();
		back.cancel();
		goAgain.cancel();
	}

}
