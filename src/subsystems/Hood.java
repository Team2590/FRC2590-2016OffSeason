package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Victor;

/**
 * We in da hood
 * @author Connor_Hofenbitzer
 */
public class Hood extends Thread implements RobotMap{
	
	private static Hood hd = null;
	
	public static Hood getInstance(){
		if(hd == null){
			synchronized(Hood.class){
				if(hd == null){
					hd = new Hood();
				}
			}
		}
		return hd;
	}
	private Victor hoodMotor;
	private double kP, kI, kD ;
	private PIDController hoodCon;
	private AnalogPotentiometer hoodPot;
	
	
	
	/**
	 * We in da hood yall
	 */
	public Hood() {
		
		//constants
		kP = 0.055;
		kI = 0.017;
		kD = 0.0;
		
		//motors and sensors
		hoodMotor = new Victor( PWM_hood );
		hoodMotor.setInverted( true );
		
		hoodPot = new AnalogPotentiometer(Analog_HoodPos, 360, -119);
		
		
		//controller settings
		hoodCon = new PIDController(kP, kI, kD, hoodPot, hoodMotor);
		hoodCon.setInputRange(3,17);
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
