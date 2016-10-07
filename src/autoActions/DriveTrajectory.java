package autoActions;

import org.usfirst.frc.team2590.robot.Robot;

import subsystems.DriveTrain;
import subsystems.DriveTrain.DriveStates;
import trajectory.DualTrajectory;
import trajectory.TrajectoryFollow;
import trajectory.TrajectoryReader;

/**
 * <b>DRIVETRAIN</b> follow a given path
 * @author Connor_Hofenbitzer
 * @see DriveTrain
 * @see TrajectoryFollow
 */
public class DriveTrajectory{

    TrajectoryFollow follower;
	DualTrajectory segs;
	boolean started = false;
	boolean reverseHeadings = false;
	
	public DriveTrajectory(String profile){
		
		TrajectoryReader reader = new TrajectoryReader(profile);
		segs = reader.getSegmentArray();
		
		follower = new TrajectoryFollow(segs);
	}
	
	public DriveTrajectory(String profile, boolean rev, double offset){
		
		TrajectoryReader reader = new TrajectoryReader(profile);
		reader.setReverseHeading(rev);
		reader.setHeadingOffset(offset);
		segs = reader.getSegmentArray();
		
		follower = new TrajectoryFollow(segs);
	}
	
	
	public boolean end() {
		return !follower.isAlive() && started;
	}

	
	public void startPath() {
		Robot.drivetrain.setState(DriveStates.TRAJECTORY);
		follower.start();
		started = true;		
	}
	
	public void cancel(){
		follower.interrupt();
	}
}
