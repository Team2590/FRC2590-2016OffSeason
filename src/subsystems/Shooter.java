package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import control.BangBangController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Victor;

/**
 * <b>SUBSYSTEM</b> All day batter shots
 * @author Connor_Hofenbitzer
 */
public class Shooter extends Thread implements RobotMap {
	
	//singleton
	private static Shooter st = new Shooter();
	
	public static Shooter getInstance(){	
		return st;
	}
	
	//state machine and variables
	private boolean auto = true;

	//other classes / sensors
	private BangBangController bang;
	private Encoder shooterEncoder;
	private Victor  shooterMotor;
	private Jelly j;

	public double getRPM() {
		return shooterEncoder.getRate();
	}

	public void run() {
		
		while( true ) {
			//update the shooter
			bang.update();
		
			//if its ready to shoot then shoot
			if(auto) {
				//allows interface between two threads (jelly and shooter in this case)
				synchronized(this) {
					if(bang.shotsFired()) {
						//if the bang bang is up to speed then shoot
						j.feedToShooter();
					} else {
						//else stop
						j.stopHT();
					}
				}
			}
			
			//if encoders at 0 , stop shooting
			if(bang.returnRPM() == 0 || 
			   bang.giveStp() == 0) {
				j.stopHT();
			}
			
		}
		
	}
	
	public Shooter() {
		
		//sensors & motors
		shooterEncoder = new Encoder(Digital_shooterEncoderA , Digital_shooterEncoderB);
		shooterEncoder.setPIDSourceType(PIDSourceType.kRate);
		shooterEncoder.setDistancePerPulse(60.0/192.0);
		shooterMotor  = new Victor(PWM_shooter);	
		
		//seperate classes
		bang = new BangBangController(shooterEncoder, shooterMotor);
		j = Jelly.getIntance();
		
		//start the thread
		this.start();
	}
	
	/**
	 * Sets the RPM to go to
	 * @param RPMSetpoint : new RPM (auto calculated with threads)
	 */
	public void setRPM(int RPMSetpoint) {
		bang.setRPMSetpoint(RPMSetpoint);
	}
	
	/**
	 * does the jelly spit when up to speed
	 * @param auto yay or neigh
	 */
	public void setAuto(boolean auto) {
		this.auto = auto;
	}
	
	/**
	 * Manually set the speed of the jelly needs to be synchronized with the jelly
	 * @param speed
	 */
	public synchronized void setFeederSpeed(int speed) {
		
		setAuto(false);
		
		switch (speed) {
		case 1: 
			j.feedToShooter();
			break;
		case -1: 
			j.deAquireBall();
			break;
		default: 
			j.stopHT();
		}
		
	}
	
	public double getSpeed(){
		return shooterEncoder.getRate();
	}
}
