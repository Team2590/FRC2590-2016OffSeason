package autoActions;

import org.usfirst.frc.team2590.robot.Robot;

import subsystems.DriveTrain;
import subsystems.DriveTrain.DriveStates;
import trajectory.DualTrajectory;
import trajectory.TrajectoryFollow;
import trajectory.TrajectoryGen;

/**
 * <b>DRIVETRAIN</b> Drive Forward given distance , max velocity and acceleration
 * @see DriveTrain.java
 * @author Connor_Hofenbitzer
 *
 */
public class DriveStraight{

	TrajectoryFollow follower;
	boolean started = false;
	
	public DriveStraight(double distance, double maxVel, double maxAcc) {
		follower = new TrajectoryFollow(
				   new DualTrajectory(
					   TrajectoryGen.generateStraightTrajectory( distance, 
							   									 maxVel, 
							   									 maxAcc)) , true);
		
	}

	
	public void run() {
		Robot.drivetrain.resetAllSensors();
		Robot.drivetrain.setState(DriveStates.TRAJECTORY);
		follower.start();
	}

	public boolean done() {
		System.out.println("followe state : " + follower.isAlive());
		return !follower.isAlive();
	}
	
	public void cancel(){
		follower.interrupt();
	}

}
