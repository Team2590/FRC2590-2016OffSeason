package autoActions;

import trajectory.DualTrajectory;
import trajectory.TrajectoryFollow;
import trajectory.TrajectoryReader;

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
		follower.start();
		started = true;		
	}
	
	public void cancel(){
		follower.interrupt();
	}
}
