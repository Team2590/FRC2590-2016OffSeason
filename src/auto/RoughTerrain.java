package auto;

import org.usfirst.frc.team2590.robot.Robot;

import autoActions.DriveStraight;
import autoActions.FixIntake;
import autoActions.Turn;
import control.Vision;
import edu.wpi.first.wpilibj.Timer;

public class RoughTerrain extends AutoModeTemplate {

	DriveStraight driveOverT , driveToTower;
	FixIntake fix;
	Turn TurnAround;
	
	@Override
	public void init() {
		driveOverT = new DriveStraight(-16, 10, 10);
		TurnAround = new Turn();
		fix = new FixIntake();
	}

	@Override
	public void run() {
		
		fix.downWithTheIntake();
		//drive over the defenses 
		driveOverT.run();
		while(!driveOverT.done()) {
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		//start the shooter
		Robot.shooter.setRPM(5600);
		Robot.hood.setHood(16);
		
		//turn around to face the target
		TurnAround.startTurn(180);
		while(!Robot.drivetrain.done()) {
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		//turn to target
		TurnAround.startTurn(Vision.getTargetOffset());
		while(!Robot.drivetrain.done()) {
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		//double check
		TurnAround.startTurn(Vision.getTargetOffset());
		while(!Robot.drivetrain.done()) {
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		//fire
		Robot.shooter.setFeederSpeed(1);

	}

	@Override
	public void cancel() {

	}

}
