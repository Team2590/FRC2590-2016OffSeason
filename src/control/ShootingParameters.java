package control;

import org.usfirst.frc.team2590.robot.Robot;

public class ShootingParameters {

	private int hoodAngle , shotSpeed;
	private boolean auto , running;
	
	public ShootingParameters(int hoodAngle , int shotSpeed , boolean auto){
		this.hoodAngle = hoodAngle;
		this.shotSpeed = shotSpeed;
		this.auto = auto;
		running = false;
	}
	
	public void setAuto(boolean auto){
		this.auto = auto;
	}
	
	public void reverseRollers(){
		auto = false;
		Robot.shooter.setFeederSpeed(-1);
	}
	/**
	 * Start the rollers , auto sets auto to false
	 */
	public void startRollers(){
		auto = false;
		Robot.shooter.setFeederSpeed(1);
		
	}
	
	/**
	 * just set auto to true
	 */
	public void stopRoller(){
		auto = true;
		Robot.shooter.setFeederSpeed(0);
	}
	
	public boolean isRunning(){
		return running;
	}
	
	/**
	 * Sets the hood and Shooter to specified parameters
	 */
	public void start(){
		running = true;
		Robot.hood.setHood(hoodAngle);
		Robot.shooter.setRPM(shotSpeed);
		Robot.shooter.setAuto(auto);
	}
	
	/**
	 * stops the shooter
	 */
	public void cancel(){
		running = false;
		Robot.shooter.setRPM(0);
		Robot.shooter.setAuto(auto);
	}
	
	
}

