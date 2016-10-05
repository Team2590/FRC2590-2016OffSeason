package auto;

import autoActions.FixIntake;

public class DoJackS extends AutoModeTemplate{

	private FixIntake intakeDown;
	
	public DoJackS(){
		intakeDown = new FixIntake();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		intakeDown.downWithTheIntake();
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}
	

}
