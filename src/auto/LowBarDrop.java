package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveTrajectory;
import autoActions.FixIntake;
import autoActions.Turn;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;

public class LowBarDrop extends AutoModeTemplate{

	DriveTrajectory dropAndRun , back;
	Turn turnToTarget;
	FixIntake fix;
	
	@Override
	public void init() {
		dropAndRun = new DriveTrajectory("low_bar_to_left_batter_drop" , true);
		back = new DriveTrajectory("Connor_pick_up_next", true , 0 	 ,false);
		
		turnToTarget = new Turn();
		fix = new FixIntake();
	}
	
	@Override
	public void run() {
		
		//aquire the ball
		Robot.intake.gimmeTehBall();
		fix.downWithTheIntake();
		Timer.delay(.8);
		
		//go to the tower
		dropAndRun.startPath();
		/*//wait until the path finishes
		waitTillDone(dropAndRun.end() , 
					 Vision.seesTarget() , 
					 () -> Robot.hood.setHood(20) , 
					 () -> Robot.shooter.setRPM(5600) );
		Robot.intake.stopBoi();

		Timer.delay(.1);
		
		
		//start the shooter
		Robot.hood.setHood(20);
		Robot.shooter.setRPM(5600);
		
		//start turning
		turnToTarget.startTurn(Vision.getTargetOffset());
		waitTillDone(Robot.drivetrain.done());
		Timer.delay(.5);

		turnToTarget.startTurn(Vision.getTargetOffset());
		waitTillDone(Robot.drivetrain.done());
		Timer.delay(.5);

		
		//shoot
		Robot.shooter.setFeederSpeed(1);
		Timer.delay(2);
		
		//stop shooters
		Robot.hood.setHood(0);
		Robot.shooter.setRPM(0);
		Robot.shooter.setFeederSpeed(0);
		
		//retreat
		Timer.delay(.5);
		Robot.intake.gimmeTehBall();
		back.startPath();*/
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
		back.cancel();
		dropAndRun.cancel();
		
	}

	
}
