package control;

import edu.wpi.first.wpilibj.Victor;

public class IntakeEnumHandler {
	
	IntakeStates state;
	Victor intakeMotor;
	int ratio;
	
	public enum IntakeStates {
		INTAKE , EXAUST , STATIONARY
	}
	
	public IntakeEnumHandler(IntakeStates state , Victor intakeMotor, int ratio){
		this.state = state;
		this.intakeMotor = intakeMotor;
		this.ratio = ratio;
	}
	
	public IntakeStates returnMode(){
		return state;
	}
	
	public void switchIntakeMode(IntakeStates state){
		this.state = state;
	}
	
	public void update(){
		switch(state) {
		
		case INTAKE:
			intakeMotor.set(1/ratio);
			break;
			
		case EXAUST:
			intakeMotor.set(-1/ratio);
			break;
			
		case STATIONARY:
			intakeMotor.set(0);
			break;
		}
	}
	
}
