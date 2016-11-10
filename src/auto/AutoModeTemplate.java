package auto;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * The autonomous template that all autos should follow
 * @author Connor_Hofenbitzer
 *
 */
public abstract class AutoModeTemplate extends Thread{
	
	/**
	 * PreStart auto
	 */
	public abstract void init();
	
	/**
	 * Run the autonomous
	 */
	public abstract void run();
	
	/**
	 * Cancel all running commands
	 */
	public abstract void cancel();
	
	
	
	protected void waitTillDone(boolean c){
		try {
			while(!c) {
				Thread.sleep(5);
			}
		}catch(Exception e){}
	}
	
	/**
	 * Waits until the checkable returns true , if when returns true then we do action
	 * @param c : a checkable method (ie boolean)
	 * @param when this is true , run a
	 * @param a : actions to run
	 */
	protected void waitTillDone(boolean c , boolean when , Action... a){
		try {
			while(!c) {
				if(when) {
					DriverStation.reportWarning("STARTING THE PARALELL COMMANDS", false);
					for(int i = 0; i < a.length; i++){
						a[i].runThis();
					}
				}
				
				Thread.sleep(5);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
