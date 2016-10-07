package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import control.IntakeEnumHandler;
import control.IntakeEnumHandler.IntakeStates;
import edu.wpi.first.wpilibj.Victor;

/**
 * <b>SUBSYSTEM</b> pushes the ball into the shooter
 * @author Connor_Hofenbitzer
 */
public class Jelly extends Thread implements RobotMap{
	
	private static Jelly j = new Jelly();
	
	public static Jelly getIntance(){
		return j;
	}
	
	private Victor shooterThingy;
	private IntakeEnumHandler handler;
	
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
