package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Victor;

/**
 * <b>SUBSYSTEM</b> We in da hood
 * @author Connor_Hofenbitzer
 */
public class Hood extends Thread implements RobotMap{
	
	private static Hood hd = new Hood();
	
	public static Hood getInstance(){
		return hd;
	}
	
	//variables
	private final double KP = 0.055;
	private final double KI = 0.016;
	
	//controllers
	private Victor hoodMotor;
	private PIDController hoodCon;
	private AnalogPotentiometer hoodPot;
	
	
	
	/**
	 * We in da hood yall
	 */
	public Hood() {
		
		
		//motors and sensors
		hoodMotor = new Victor( PWM_hood );
		hoodMotor.setInverted( true );
		
		hoodPot = new AnalogPotentiometer(Analog_HoodPos, 360, -119);
		
		//controller settings
		hoodCon = new PIDController(KP, KI, 0, hoodPot, hoodMotor);
		hoodCon.setInputRange(3.5,20);
		hoodCon.setOutputRange(-.4, .4);
		hoodCon.setAbsoluteTolerance(.5);
	}
	
	/**
	 * Sets the setPoint it should go to
	 * @param angle angle to go to
	 */
	public void setHood( double angle ) { 
		hoodCon.setSetpoint( angle  );
		
		hoodCon.enable( );
	}
	

}
