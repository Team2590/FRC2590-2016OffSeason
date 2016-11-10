package control;

import edu.wpi.first.wpilibj.Victor;

/**
 * <b>CONTROL STRUCTURE</b> anything to do with moving the ball can be done with this
 * @author Connor_Hofenbitzer
 *
 */
public class IntakeEnumHandler {
	
	IntakeStates state;
	Victor intakeMotor;
	
	public enum IntakeStates {
		INTAKE , EXAUST , STATIONARY
	}
	
	public IntakeEnumHandler(IntakeStates state , Victor intakeMotor) {
		this.state = state;
		this.intakeMotor = intakeMotor;
	}
	
	public IntakeStates returnMode() {
		return state;
	}
	
	public void switchIntakeMode(IntakeStates state) {
		this.state = state;
	}
	
	public void update() {
		switch(state) {
		
		case INTAKE:
			intakeMotor.set(1);
			break;
			
		case EXAUST:
			intakeMotor.set(-1);
			break;
			
		case STATIONARY:
			intakeMotor.set(0);
			break;
			
		}
	}
	
}
