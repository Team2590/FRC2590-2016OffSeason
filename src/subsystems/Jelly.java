package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import control.IntakeEnumHandler;
import control.IntakeEnumHandler.IntakeStates;
import edu.wpi.first.wpilibj.Victor;

public class Jelly extends Thread implements RobotMap{
	
	private static Jelly j = null;
	
	public static Jelly getIntance(){
		if(j == null){
			synchronized(Jelly.class){
				if(j == null){
					j = new Jelly();
				}
			}
		}
		
		return j;
	}
	
	private IntakeEnumHandler handler;
	private Victor shooterThingy;
	
	public void run() {
		while(true) {
			synchronized(this){
			handler.update();
			}
		}
	}
	
	public Jelly() {
		shooterThingy = new Victor(PWM_jelly);
		handler = new IntakeEnumHandler(IntakeStates.STATIONARY , shooterThingy);
		this.start();
	}
	
	/**
	 * Pow Pow shooty bang bang
	 */
	public synchronized void feedToShooter() {
		handler.switchIntakeMode(IntakeStates.INTAKE);
	}
	
	/**
	 * I dont want this ball anymore 
	 */
	public synchronized void deAquireBall() {
		handler.switchIntakeMode(IntakeStates.EXAUST);
	}
	
	/**
	 * Stop! Hammer Time!
	 */
	public synchronized void stopHT() {
		handler.switchIntakeMode(IntakeStates.STATIONARY);
	}
	
	
	
	
}
