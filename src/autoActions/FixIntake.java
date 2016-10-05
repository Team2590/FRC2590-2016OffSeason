package autoActions;

import edu.wpi.first.wpilibj.Timer;
import subsystems.Arm;

public class FixIntake{

	private boolean done = false;
	private Arm inta;
	 
	public FixIntake(){
		inta = Arm.getInstance();
	}
	
	/**
	 * Just puts the intake down
	 */
	public void downWithTheIntake() {
		inta.setMotorSpeed(-0.7);
		Timer.delay(.4);
		inta.setMotorSpeed(0);
		inta.setEnabled(true);
		done = true;
		
	}

	public boolean end() {
		// TODO Auto-generated method stub
		return done;
	}
}
