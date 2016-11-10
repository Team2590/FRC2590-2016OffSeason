package trajectory;

import org.usfirst.frc.team2590.robot.Robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TrajectoryFollow extends Thread {
	DualTrajectory traj;
	boolean done = false;
	public TrajectoryPIDController leftController, rightController;

	public TrajectoryFollow(DualTrajectory traj , boolean reset) {
		this.traj = traj;
		leftController = new TrajectoryPIDController(CC.kStraightP, CC.kStraightI, CC.kStraightD, 
				CC.kStraightV, CC.kStraightA, CC.kStraightTurn, Robot.drivetrain.leftE, Robot.drivetrain.leftM, Robot.drivetrain.gyro);
		rightController = new TrajectoryPIDController(CC.kStraightP, CC.kStraightI, CC.kStraightD, 
				CC.kStraightV, CC.kStraightA, CC.kStraightTurn, Robot.drivetrain.rightE, Robot.drivetrain.rightM, Robot.drivetrain.gyro);
		rightController.setTurnDirection(true);		
	}
	
	public void run() {
		
		long start = System.currentTimeMillis();
		
		leftController.reset();
		leftController.enable();
		rightController.reset();
		rightController.enable();

	
		Robot.drivetrain.leftE.reset();
		Robot.drivetrain.rightE.reset();
			
		Robot.drivetrain.gyro.reset();
		
			
		System.out.println("Running traj");
		int segment = 0;
		
		while(segment < traj.left.size()) {
			//System.out.println("in traj loop");
			
			long current = System.currentTimeMillis();
			long dif = current - start;
			
			leftController.setSetpoint(traj.left.get((int) segment));
			rightController.setSetpoint(traj.right.get((int) segment));
			
			SmartDashboard.putNumber("Left Trajectory Pos: ", traj.left.get((int) segment).pos);
			SmartDashboard.putNumber("Right Trajectory Pos: ", traj.right.get((int) segment).pos);
			
			//System.out.println("Setting Setpoint: " + traj.left.get((int) segment).pos);
			segment = (int) Math.round(dif / ((traj.left.get(0).dt) * 1000));	
			
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		
		done = true;

		System.out.println("kicked out m8");
		

		rightController.disable();
		leftController.disable();


		//Robot.drivetrain.stopMotors();
	}
	
	public boolean isFinishedPath() {
		return done;
	}
	
	
}