package control;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

public class BangBangController {
	
	//constants
	private int RPMSetpoint;
	private boolean readyToShoot;
	private final double TOLERANCE = 50;
	
	//sensors
	private Encoder RPMCounter;
	private Victor shooterMotor;
	
	/**
	 * Typical bang bang controller
	 * @param RPMCounter : the encoder that measures RPM
	 * @param shooterMotor : the shooter motor
	 */
	public BangBangController(Encoder RPMCounter , Victor shooterMotor){
		RPMSetpoint = 0;
		readyToShoot = false;
		this.RPMCounter = RPMCounter;
		this.shooterMotor = shooterMotor;
	}
	
	/**
	 * Set the setpoint
	 * @param RPMSetpoint where do you want to go?
	 */
	public void setRPMSetpoint(int RPMSetpoint){
		this.RPMSetpoint = RPMSetpoint;
	}
	
	/**
	 * Update the motors to go to the RPM setpoint
	 */
	public void update(){
		//are we ready to fire
		readyToShoot = (-RPMCounter.getRate() < (RPMSetpoint + TOLERANCE/2.0)) &&
					   (-RPMCounter.getRate() > (RPMSetpoint - TOLERANCE/2.0) && 
					     RPMSetpoint != 0);
		
		//if we are that means we are above setpoint and need to slow down , else  fire away
		if(  -RPMCounter.getRate() < ( RPMSetpoint - TOLERANCE ) && RPMSetpoint != 0 ){
			shooterMotor.set(1);
		}else if( -RPMCounter.getRate() > ( RPMSetpoint + TOLERANCE ) && RPMSetpoint != 0){
			shooterMotor.set(0.5);
		}else if( RPMSetpoint == 0 ){
			shooterMotor.set(0);
		}	
	}
	
	/**
	 * Are we ready to shoot?
	 * @return ready to shoot
	 */
	public boolean shotsFired(){
		return readyToShoot;
	}
	
	/**
	 * What arm are we trying to get to?
	 * @return RPMSetpoint
	 */
	public int returnRPM(){
		return RPMSetpoint;
	}
}
