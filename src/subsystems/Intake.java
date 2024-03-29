package subsystems;

import org.usfirst.frc.team2590.robot.RobotMap;

import control.IntakeEnumHandler;
import control.IntakeEnumHandler.IntakeStates;
import edu.wpi.first.wpilibj.Victor;

/**
 * <b>SUBSYTEM</b> Sucks in the ball and spits it out
 * @author Connor_Hofenbitzer
 */
public class Intake extends Thread implements RobotMap{

	//singleton
	private static Intake in = new Intake();
	
	public static Intake getInstance() {
		return in;
	}
	
	//state machine and motor
	private Victor intakeMotor;
	private IntakeEnumHandler handler;

	public void run() {
		while(true) {
			//update the handler for the rollers
			handler.update();		
		}
	}
	
	/**
	 * Intake
	 */
	public void gimmeTehBall() {
		handler.switchIntakeMode(IntakeStates.INTAKE);
	}
	
	/**
	 * Outake
	 */
	public void spitOut() {
		handler.switchIntakeMode(IntakeStates.EXAUST);
	}
	
	/**
	 * Stop the motor
	 */
	public void stopBoi() {
		handler.switchIntakeMode(IntakeStates.STATIONARY);
	}
	
	/**
	 * You have to be pretty thick to not know what this code is 
	 */
	public Intake() {
		intakeMotor = new Victor(PWM_intakeRollers);
		handler = new IntakeEnumHandler(IntakeStates.STATIONARY, intakeMotor);
		this.start();
	}
}
