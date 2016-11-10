package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import auto.Checkable;
import control.PID;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * <b>SUBSYSTEM</b> Drive Train
 * @author Connor_Hofenbitzer
 */
public class DriveTrain extends Thread implements RobotMap , Checkable{
	
	//state controller
	public enum DriveStates { 
		TELEOP , TURNING , TRAJECTORY , DRIVESTRAIGHT , LOCKED 
	};
	private DriveStates currentState;
	
	//sensors
	public Encoder leftE;
	public Encoder rightE;
	public AnalogGyro gyro;

	//drive
	public RobotDrive driveTrain;
	
	//motors
	public Victor leftM;
	public Victor rightM;
	
	//joysticks
	private Joystick x;
	private Joystick y;
	
	//PIDs
	private PID turnP;
	
	//booleans
	private boolean done = false;
	private boolean first = true;
	
	//constants
	private int lastTime = 0;
	private final double TOLERANCE = 2;	

	
	public void run() {		
		while( true ) {
			//SmartDashboard.putNumber("gyro", gyro.getAngle());
			SmartDashboard.putNumber("lefte", leftE.getDistance());
			SmartDashboard.putNumber("righte", rightE.getDistance());

			if(currentState == DriveStates.TELEOP ) {
				//if we are driving in teleop
				rightM.setInverted(false);
				inputDrive();
				
			} else if(currentState == DriveStates.TURNING || 
					  currentState == DriveStates.TRAJECTORY) {
				//if doing auto stuff 
				rightM.setInverted(true);

			} 	else if(currentState == DriveStates.DRIVESTRAIGHT) {
				//pretty much just dont let us drive with joystick
				rightM.setInverted(false);
				//let it do whatever it wants
			}	else {
				//otherwise
				stopMotors();
				
			}	
			
		}	
	}
		
	
	
	public DriveTrain( Joystick x , Joystick y , Encoder leftE , Encoder rightE) {
		
		//joysticks
		this.x = x;
		this.y = y;
				
		//motors
		this.leftE = leftE;
		this.rightE = rightE;
		leftM = new Victor(PWM_driveLeft);
		rightM = new Victor(PWM_driveRight);
		
		
		//driveTrain
		driveTrain = new RobotDrive(leftM, rightM);
		
		//encoders && other sensors
		gyro = new AnalogGyro(0);
		
        
		//PID Controllers T = turn
		turnP = new PID(KP , KI , KD , true , 0.4);
		
		//start the thread
		this.start();
		 
	}
	
	/**
	 * Drive with Joystick input
	 */
	private void inputDrive( ) {
		driveTrain.arcadeDrive(-x.getY(), -y.getX());
	}
	
	/**
	 * Sets autonomous mode
	 * @param auto : locks the controls if true
	 */
	public void setState( DriveStates drive ) {
		//set the drive state
		currentState = drive;
		
		//get ready for the trajectory and turning stuff

	
		if(drive == DriveStates.TURNING || drive == DriveStates.DRIVESTRAIGHT) {
			resetAllSensors();
			first = true;
			done = false;
			lastTime = (int) Timer.getFPGATimestamp();
		}
		
	}
	
	/**
	 * Forces the driveBase into teleoperated
	 */
	public void forceTeleop(){
		
		DriverStation.reportWarning("Forcing current state to Teleop " , false);
		currentState = DriveStates.TELEOP;
		
	}
	
	/**
	 * Its self explanitory
	 * @param angle : the angle you would like to turn to
	 */
	public synchronized void turnToAngle( double angle ) {
		
		//print to know were turning 
		System.out.println("turn being called");
		
		//if its the first ca;; set the angle
		if( first ) {
			turnP.set(angle);
			first = false;
		}
		
		//if were not done were still turning , obvi 
		if( !done ) {
				
			//set the outputs to the motors
			leftM.set(turnP.getOutput(gyro.getAngle()));
			rightM.set(-turnP.getOutput(gyro.getAngle()));
		
			//end condition
			if( Math.abs( turnP.getSetpoint() - gyro.getAngle() ) < TOLERANCE || 
					Math.abs( lastTime - Timer.getFPGATimestamp() ) > 3 ) { 
				
						//if were done then lock everything
						done = true;
						currentState = DriveStates.LOCKED;
						System.out.println("done dt set to locked");	
			}
		}
	
	}
	
	/**
	 * Reset every drivetrain sensor
	 */ 
	public synchronized void resetAllSensors() {
		
		//don't be an idiot ,  you know what this does
		gyro.reset();
		leftE.reset();
		rightE.reset();
	}

	@Override
	public boolean done() {
		
		return done;
	}
	
	/**
	 * Stops all motors
	 */
	public void stopMotors() {
		
		driveTrain.arcadeDrive(0,0);
	}
}
