package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Victor;

/**
 * <b>SUBSYSTEM</b> Manipulating Arm
 * @author Connor_Hofenbitzer
 */
public class Arm extends Thread implements RobotMap{
	
	//singleton
	private static Arm arm = new Arm();
	
	public static Arm getInstance() {
		return arm;
	}

	
	//variables
	private final double KP = 0.015;
	private final double KI = 0.0000175;
	private final double ZEROPOSITION = 12;

	//controllers
	private Victor armMotor;
	private AnalogPotentiometer armPot;
	private PIDController armController; 
	
	//gearing ratio
	private static final double GEAR_RATIO = (42.0/15.0);


	
	/**
	 * Intake Manipulator , used for things such as chival de frise 
	 */
	public Arm(){
		
		//motors and sensors
		armMotor = new Victor(PWM_intakeArticulate);
		armPot = new AnalogPotentiometer(Analog_IntakePos , -360 , 360);
		armPot.setPIDSourceType(PIDSourceType.kDisplacement);

		//PID controller and its settings
		armController = new PIDController(KP , KI , 0 , armPot , armMotor);
		armController.setInputRange(0 , 360);
		armController.setOutputRange(-.2 , .5);
		armController.setAbsoluteTolerance(.5);
		
		
	}
	
	/**
	 * Arm goes to the angle
	 * @param setPoint angle to go to
	 */
	public void setSetpoint(double setPoint) {
	
		//math which calulates offset , and that stuff
		double angle = (setPoint * GEAR_RATIO) + ZEROPOSITION;
		
		//conserve power
		if( setPoint < 7 ) {
			setMotorSpeed(0);
		} else {
			//reset I constant and set the setPoint then go to that angle
			armController.reset();
			armController.setSetpoint(angle);
			armController.enable();
		}
		
	}
	
	/**
	 * Set the motor speed
	 * @param speed
	 */
	public void setMotorSpeed(double speed){
		armController.disable();
		armMotor.set(speed);
	}
	
	
	public double returnAngle(){
		return armPot.pidGet() / GEAR_RATIO - ZEROPOSITION;
	}
	
	
}
