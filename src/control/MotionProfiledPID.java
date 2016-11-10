package control;

import edu.wpi.first.wpilibj.Timer;

/**
 * <b>CONTROLLER</b> A motion profiled feed forward loop 
 * <br>
 * controls for acceleration , velocity and position
 * @author Connor_Hofenbitzer
 *
 */
public class MotionProfiledPID {
	
	private double kP;
	private double kD;
	private double kV;
	private double kA;
	private double maxAcc;
	private double maxVel;
	private double lastError;
	private double lastSetpoint;
	private double lastTime;
	private double errorSum;
	private double posSetPoint;
	private double accSetPoint;
	private double velSetPoint;
	
	/**
	 * A motion profiled PID loop with 3 states , position , acceleration , velocity , integral is generally not needed.
	 * Max acceleration and velocity are both defaulted to 8
	 * @param kP proportional gain
	 * @param kD deteritive gain
	 * @param kV feed forward velocity 
	 * @param kA feed forward acceleration
	 */
	public MotionProfiledPID(double kP , double kI , double kD , double kV , double kA) {
		
		maxAcc = 8;
		maxVel = 8;
		this.kP = kP;
		this.kD = kD;
		this.kV = kV;
		this.kA = kA;
		posSetPoint = 0;
		accSetPoint = 0;
		velSetPoint = 0;
		
	}
	
	/**
	 * Set the maxes 
	 * @param maxAcc max acceleration 
	 * @param maxVel max velocity
	 */
	public void setMax(double maxAcc , double maxVel) {
		
		//set the max accelerations
		this.maxAcc = maxAcc;
		this.maxVel = maxVel;
		
	}
	
	/**
	 * set the setpoint to go to 
	 * @param posSetPoint
	 * @param accSetpoint
	 * @param velSetpoint
	 */
	public void setSetpoint(double posSetPoint , double accSetpoint , double velSetpoint ) {
		
		//if the given stps are greater than max just set them to max
		accSetPoint = (accSetpoint < maxAcc) ? accSetpoint : maxAcc;
		velSetPoint = (velSetpoint < maxVel) ? velSetpoint : maxVel;
		this.posSetPoint = posSetPoint;
		
	}
	
	/**
	 * Calculate the output of the PID loop
	 * @param processVariable the sensor input
	 * @return the motor output
	 */
	public double calculate(double processVariable) {
		
		//get your error
		double error = posSetPoint - processVariable;
		
		//if setpoint changes reset the last stuffs
		if(posSetPoint != lastSetpoint) {
			lastError = error;
			lastTime  = Timer.getFPGATimestamp() * 1000;
		}
		

		double time = Timer.getFPGATimestamp() * 1000;
		double dt = time - lastTime;
		
		errorSum += error * dt; //always integrate error
		
		//get the values for time and error changes
		double deltaError = (error - lastError) / dt;
		
		//calculate output
		double outPut = kP * error + 
						kD * deltaError + 
						kV * velSetPoint + 
						kA * accSetPoint;
				
		//set all lasts
		lastSetpoint = posSetPoint;
		lastError = error;
		
		//return
 		return outPut;
 		
	}
}
