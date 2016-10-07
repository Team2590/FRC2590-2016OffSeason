package control;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import edu.wpi.first.wpilibj.Timer;

/**
 * <b>CONTROLLER</b> A PID like controller which uses previous sensor values 
 * to predict what speed the robot should go, 
 * assumes that gyro is reset before started
 * @author Connor_Hofenbitzer
 */
public class AdaptiveController {

	//variables
	double degredationValue;
	double lastSetpoint;
	double startTime;
	double lookAhead;
	double setpoint;
	double error;
	double kP;
	
	//constants
	int maxSpeed;
	int minSpeed;
	
	boolean done; 
	
	//regression calculator
	SimpleRegression CVP;
	
	public AdaptiveController(double degredationValue , double lookAhead) {
		
		this.degredationValue = degredationValue;
		this.lookAhead = lookAhead;
		this.lastSetpoint = 0;
		this.setpoint = 0;
		this.error = 0;
		
		done = false;
		
		startTime = Timer.getFPGATimestamp();
		
		CVP = new SimpleRegression();
	}
	
	public void setMaxMinSpeeds(int maxSpeed , int minSpeed) { 
		this.maxSpeed = maxSpeed;
		this.minSpeed = minSpeed;
	}
	
	/**
	 * Set the setPoint to go to
	 * @param setpoint setpoint to go to
	 */
	public void setSetpoint(double setpoint) {
		lastSetpoint = this.setpoint;
		this.setpoint = setpoint;
		
		if(setpoint != 0) { 
			if(setpoint > 0) {
				kP = 0.5;
			} else {
				kP -= 0.5;
			}
		}
	}
	
	/**
	 * Calculate the output
	 * @param processVariable : sensor value
	 * @return motor value
	 */
	public double calculate(double processVariable) {
		
		if(setpoint != lastSetpoint){
			CVP.clear();
			done = false;
			startTime = Timer.getFPGATimestamp();
		}
		
		error = Math.abs(setpoint - processVariable);
		CVP.addData(Timer.getFPGATimestamp() - startTime, processVariable);		
		double nextValue = CVP.predict( (Timer.getFPGATimestamp() - startTime) + lookAhead );
		
		//overshooting less than to greater than
		if(nextValue > setpoint && setpoint > 0){
			kP -= degredationValue;
		}else if(nextValue < setpoint && setpoint < 0){
			kP += degredationValue;
		}
		
		if(kP > maxSpeed){
			kP = maxSpeed;
		}else if(kP < minSpeed){
			kP = minSpeed;
		}
		
		if(error <= 1) done = true;
		return kP;
	}
	
	/**
	 * Are we on target
	 * @return done
	 */
	public boolean onTarget(){
		return done;
	}
}
