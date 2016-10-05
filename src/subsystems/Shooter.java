package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import control.BangBangController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Victor;

/**
 * All day batter shots
 * @author Connor_Hofenbitzer
 *
 */
public class Shooter extends Thread implements RobotMap {
	
	private static Shooter st = null;
	
	public static Shooter getInstance(){	
		if(st == null){
			//thread safe
			synchronized(Shooter.class){
				if(st == null){
					st = new Shooter();
				}
			}
		}
		return st;
	}
	
	private boolean auto , tempForceFire;
	private BangBangController bang;
	private Encoder shooterEncoder;
	private Victor  shooterMotor;
	private Jelly j;

	

	//so I know someone will ask tempForce fire means that if we get to setRPM it forces the 
	//jelly to shoot , even if we go below RPM
	public void run() {
		while( true ) {

			//update the shooter
			bang.update();
			
			//force the shot if we get above the threashold
			if(bang.shotsFired()){
				tempForceFire = true;
			} 
			
			//if its ready to shoot then shoot
			if(auto) {
				//allows interface between two threads
				synchronized(this){
				/*if(tempForceFire)
					j.feedToShooter();
				else
					j.stopHT();*/
				}
			} 
			
			//if its at 0 , stop shooting
			if(bang.returnRPM() == 0){
				tempForceFire = false;
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
		j = new Jelly();
		
		//constants
		auto = true;
		
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
	 * Manually set the speed of the jelly
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
	
}
