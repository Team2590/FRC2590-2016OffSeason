package autoActions;

import trajectory.DualTrajectory;
import trajectory.TrajectoryFollow;
import trajectory.TrajectoryGen;

public class DriveStraight{

	TrajectoryFollow follower;
	boolean started = false;
	
	public DriveStraight(double distance, double maxVel, double maxAcc){
		follower = new TrajectoryFollow(new DualTrajectory(TrajectoryGen.generateStraightTrajectory(distance, maxVel, maxAcc)));
	}

	
	public void run() {
		follower.start();
		started = true;
	}

	public boolean done() {
		return follower.isFinishedPath() && started;
	}
	
	public void cancel(){
		follower.interrupt();
	}

}
