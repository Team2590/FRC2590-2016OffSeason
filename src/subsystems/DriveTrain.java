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

/**
 * <b>SUBSYSTEM</b> Drive Train
 * @author Connor_Hofenbitzer
 */
public class DriveTrain extends Thread implements RobotMap , Checkable{
	
	//state controller
	public enum DriveStates { TELEOP , TURNING , TRAJECTORY , LOCKED };
	private DriveStates currentState;
	
	//sensors
	public Encoder leftE;
	public Encoder rightE;
	public AnalogGyro gyro;

	//drive
	private RobotDrive driveTrain;
	
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
	private final double KP = 0.1;
	private final double KI = 0.006;
	private final double KD = 0.006;	
	private final double TOLERANCE = 2;
	
	private int lastTime = 0;

	
	public void run() {
		
		while( true ) {
			switch(currentState) {
			
			case TELEOP :
				rightM.setInverted(false);
				leftM.setInverted(false);
				inputDrive();
				break;
				
			case TURNING : 
				rightM.setInverted(true);
				break;
				
			case TRAJECTORY :
				rightM.setInverted(true);
				break;
				
			case LOCKED :
				stopMotors();
				break;
				
			default :
				DriverStation.reportError("Unsupported drive state! ", false);
				
			}
			
		}
		
	}
	
	public DriveTrain( Joystick x , Joystick y) {
		
		//joysticks
		this.x = x;
		this.y = y;
				
		//sensors and motors
		leftM = new Victor(PWM_driveLeft);
		rightM = new Victor(PWM_driveRight);
		
		//driveTrain
		driveTrain = new RobotDrive(leftM, rightM);
		
		//encoders
		leftE = new Encoder(2,3);
        rightE = new Encoder(1,0);
		gyro = new AnalogGyro(0);
		
		
        leftE.setDistancePerPulse(1.0/360.0 * ((6.0 * Math.PI) / 12.0));
        rightE.setDistancePerPulse(1.0/360.0 * ((6.0 * Math.PI) / 12.0));
        
		//PID Controllers T = turn
		turnP = new PID(KP,KI,KD,false);
		
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
		
		currentState = drive;
		
		resetAllSensors();
		first = true;
		done = false;
		lastTime = (int) Timer.getFPGATimestamp();
		
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
		
		if( first ) {
			turnP.set(angle);
			first = false;
		}
		
		if( !done ) {
				
			//set the outputs to the motors
			leftM.set(turnP.getOutput(gyro.getAngle()));
			rightM.set(-turnP.getOutput(gyro.getAngle()));
		
			//end condition
			if( Math.abs( turnP.getSetpoint() - gyro.getAngle() ) < TOLERANCE 
					 || Math.abs( lastTime - Timer.getFPGATimestamp() ) > 3 ) { 
				
				first = true;
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
