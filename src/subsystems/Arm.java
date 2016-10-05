package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Victor;

/**
 * Manipulating Arm
 * @author Connor_Hofenbitzer
 *
 */
public class Arm extends Thread implements RobotMap{
	
	private double kP , kI, kD , zeroPosition;
	private PIDController armController; 
	private AnalogPotentiometer armPot;
	private Victor armMotor;
	private boolean enabled;
	
	private static final double GEAR_RATIO = (42.0/15.0);

	/**
	 * Intake Manipulator , used for things such as chival de frise 
	 */
	public Arm(){
		//constants
		kP = 0.015;
		kI = 0.0000175;
		enabled = true;
		zeroPosition = 12;

		//motors and sensors
		armMotor = new Victor(PWM_intakeArticulate);
		armPot = new AnalogPotentiometer(Analog_IntakePos , -360 , 360);
		armPot.setPIDSourceType(PIDSourceType.kDisplacement);

		//PID controller and its settings
		armController = new PIDController(kP , kI , kD , armPot , armMotor);
		armController.setInputRange(0 , 360);
		armController.setOutputRange(-.2 , .5);
		armController.setAbsoluteTolerance(.5);
		this.start();
	}
	
	/**
	 * Arm goes to the angle
	 * @param setPoint angle to go to
	 */
	public void setSetpoint(double setPoint){
		
		//math which calulates offset , and that stuff
		double angle = (setPoint * GEAR_RATIO) + zeroPosition;
		
		//reset I constant and set the setPoint then go to that angle
		armController.reset();
		armController.setSetpoint(angle);
		
		if(enabled){
			armController.enable();
		}

	}
	
	/**
	 * Set the motor speed
	 * @param speed
	 */
	public void setMotorSpeed(double speed){
		enabled = false;
		armMotor.set(speed);
	}
	
	/**
	 * Set enabled 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
}
