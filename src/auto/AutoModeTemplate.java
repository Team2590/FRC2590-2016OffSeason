package auto;

public abstract class AutoModeTemplate extends Thread{
	
	public abstract void run();
	public abstract void cancel();
	
	
	protected void waitTillDone(Checkable c){
		try {
			while(!c.done()){
				Thread.sleep(5);
			}
		}catch(Exception e){}
	}
	
	/**
	 * Waits until the checkable returns true , if when returns true then we do action
	 * @param c : a checkable method (ie boolean)
	 * @param when this is true , run a
	 * @param a : an action to run
	 */
	protected void waitTillDone(Checkable c , boolean when , Action a){
		try {
			while(!c.done()){
				if(when) a.runThis();
				Thread.sleep(5);
			}
		}catch(Exception e){}
	}
	
}
