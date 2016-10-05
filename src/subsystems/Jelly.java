package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import control.IntakeEnumHandler;
import control.IntakeEnumHandler.IntakeStates;
import edu.wpi.first.wpilibj.Victor;

public class Jelly extends Thread implements RobotMap{
	
	private IntakeEnumHandler handler;
	private Victor shooterThingy;
	
	public void run() {
		while(true) {
			handler.update();
		}
	}
	
	public Jelly() {
		shooterThingy = new Victor(PWM_jelly);
		handler = new IntakeEnumHandler(IntakeStates.STATIONARY , shooterThingy, 2);
		this.start();
	}
	
	/**
	 * Pow Pow shooty bang bang
	 */
	public void feedToShooter() {
		handler.switchIntakeMode(IntakeStates.INTAKE);
	}
	
	/**
	 * I dont want this ball anymore 
	 */
	public void deAquireBall() {
		handler.switchIntakeMode(IntakeStates.EXAUST);
	}
	
	/**
	 * Stop! Hammer Time!
	 */
	public void stopHT() {
		handler.switchIntakeMode(IntakeStates.STATIONARY);
	}
	
	
	
	
}
