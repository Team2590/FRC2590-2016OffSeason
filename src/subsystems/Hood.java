package subsystems;

import org.usfirst.frc.team2590.robot.Robot;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * <b>SUBSYSTEM</b> We in da hood
 * @author Connor_Hofenbitzer
 */
public class Hood extends Thread implements RobotMap{
	
	//singleton
	private static Hood hd = new Hood();
	
	public static Hood getInstance() {
		return hd;
	}
	
	//state machine
	private enum hoodState { 
		WANTTOEXTEND , WANTTORETRACT , STOPPED 
	};
	private hoodState state = hoodState.STOPPED;
	
	//variables
	private final double KP = 0.055;
	private final double KI = 0.016;
	private boolean allowed = false;
	private final double CLEARANCETOL = 5;
	private final double ARMCOLLISION = 90;

	
	//controllers
	private Victor hoodMotor;
	private PIDController hoodCon;
	private AnalogPotentiometer hoodPot;
	
	
	public void run() {
		while(true)   {
			switch(state) {
			
			case WANTTOEXTEND :
				
				//if the arm is up and would crash with hood , put arm down first
				if(Robot.arm.returnAngle() >= ARMCOLLISION) {
									
					if(Robot.arm.returnAngle() >= 105) {
						//sequential actions
						allowed = false;
						Robot.arm.setMotorSpeed(-.6);
						Timer.delay(.8);
						
						//then lock the arm
						Robot.arm.setMotorSpeed(0);
						Robot.arm.setSetpoint(0);
						allowed = true;
						
					}
					
					if(Robot.arm.returnAngle() > ARMCOLLISION - CLEARANCETOL && 
					   Robot.arm.returnAngle() < 105 ) {
								//sequential actions
								allowed = false;
								Robot.arm.setSetpoint(0);
								allowed = true;
						
					}

				}
				
				//if everythings run through... ill allow it
				allowed = Robot.arm.returnAngle() < ARMCOLLISION - CLEARANCETOL;
				
				break;
				
			case WANTTORETRACT :
				//nothing ever goes wrong when your trying to go down
				allowed = true;
				break;
				
			case STOPPED :
				allowed = false;
				break;
			}
			
			//if were on target , reserve power by stopping the hood
			if(hoodCon.onTarget()) {
				//state = hoodState.STOPPED;
			}
			SmartDashboard.putNumber("hood", hoodPot.pidGet());
			//if were allowed to move then move
			if(allowed) {
				hoodCon.enable();
			} else {
				hoodCon.disable();
			}
			
		}
	}
	
	/**
	 * We in da hood yall
	 */
	public Hood() {
		
		
		//motors and sensors
		hoodMotor = new Victor( PWM_hood );
		hoodMotor.setInverted( true );
		
		// init the pot
		hoodPot = new AnalogPotentiometer(Analog_HoodPos, 360, -119);
		
		//controller settings we dont need a D term
		hoodCon = new PIDController(KP, KI, 0, hoodPot, hoodMotor);
		hoodCon.setInputRange(3.5,19);
		hoodCon.setOutputRange(-.4, .4);
		hoodCon.setAbsoluteTolerance(.5);
		
		//start the thread
		this.start();
		
	}
	
	/**
	 * Sets the setPoint it should go to
	 * @param angle angle to go to
	 */
	public void setHood( double angle ) { 

		//retract or extend based on current position
		state = (angle > hoodPot.pidGet()) ? hoodState.WANTTOEXTEND : 
											 hoodState.WANTTORETRACT;
		
		//set the angle
		hoodCon.setSetpoint( angle );
	}
	
	/**
	 * get the current angle of the hood
	 * @return get the current angle of the hood
	 */
	public double returnHoodAngle(){
		return hoodPot.pidGet();
	}

}
