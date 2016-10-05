package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import auto.Checkable;
import control.PID;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

/**
 * Drive Train
 * @author Connor_Hofenbitzer
 *
 */
public class DriveTrain extends Thread implements RobotMap , Checkable{
	
	public Encoder leftE , rightE;
	private RobotDrive driveTrain;
	public Victor leftM , rightM;
	public AnalogGyro gyro;
	private Joystick x , y;
	private PID turnP;
	
	private boolean auto , done , first;
	private final double TOLERANCE;
	private double tP , tI , tD;
	private int lastTime = 0;

	
	public void run( ) {
		while( true ) {
			//we may only drive if we are not in auto mode
			if( !auto ){
				rightM.setInverted(false);
				leftM.setInverted(false);
				inputD( );
			}else if (auto){
				rightM.setInverted(true);
			}
						
		}
	}
	
	public DriveTrain( Joystick x , Joystick y , Encoder LeftE , Encoder RightE ) {
		
		//turning PID values
		tP = 0.1;
		tI = 0.006;
		tD = 0.006;
		
		//joysticks
		this.x = x;
		this.y = y;
		
		//booleans
		auto = true;
		done = false;
		first = true;
		TOLERANCE = 2;
		
		//sensors and motors
		leftM = new Victor(PWM_driveLeft);
		rightM = new Victor(PWM_driveRight);
		
		//driveTrain
		driveTrain = new RobotDrive(leftM, rightM);
		
		//encoders
		leftE = LeftE;
		rightE = RightE;
		gyro = new AnalogGyro(0);
		
		//PID Controllers T = turn
		turnP = new PID(tP,tI,tD,false);
		
		//start the thread
		this.start();
		 
	}
	
	/**
	 * Drive with Joystick input
	 */
	public void inputD( ) {
		//drive with input
		driveTrain.arcadeDrive(-x.getY(), -y.getX());
	}
	
	/**
	 * Sets autonomous mode
	 * @param auto : locks the controls if true
	 */
	public void setAuto( boolean auto ) {
		this.auto = auto;
		gyro.reset();
		first = true;
		done = false;
		lastTime = (int) Timer.getFPGATimestamp();
		
	}
	
	
	/**
	 * Its self explanitory
	 * @param angle : the angle you would like to turn to
	 */
	public void turnToAngle( double angle ) {
		
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
				System.out.println("done");
				
			}
		}
	
	}
	
	/**
	 * Reset every drivetrain sensor
	 */ 
	public void resetAllSensors(){
		gyro.reset();
		leftE.reset();
		rightE.reset();
	}

	@Override
	public boolean done() {
		return done;
	}
	
	public void stopMotors(){
		driveTrain.arcadeDrive(0,0);
	}
}
