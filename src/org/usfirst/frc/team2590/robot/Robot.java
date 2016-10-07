
package org.usfirst.frc.team2590.robot;

import autoActions.Turn;
import control.Vision;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import subsystems.Arm;
import subsystems.DriveTrain;
import subsystems.DriveTrain.DriveStates;
import subsystems.Hood;
import subsystems.Intake;
import subsystems.Shooter;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot implements RobotMap{
    
    //intake angles
    private final int STRAIGHTPICK = 70;
    private final int CHIVALPICK = 45;
    private final int FLOORPICK = 0;
    
    //other variables
    private boolean shooting = false; 
    private boolean firing = false;
    private double startTime = 0; 
    private double now = 0;
  
    //sensors
    private Joystick left , right , operator;

    //subsystems and that stuff
    public static DriveTrain drivetrain;
    public static Shooter shooter;
    public static Intake intake;
    public static Hood hood;
    public static Arm arm;
    
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	//joysticks
        operator = new Joystick(2);
        right = new Joystick(1);
        left = new Joystick(0);
        
       
        //implements a singleton design pattern to assure that only one
        //class uses a class and its motors at one time
        drivetrain = new DriveTrain(left, right);
        shooter = Shooter.getInstance();
        intake = Intake.getInstance();
        hood = Hood.getInstance();
        arm = Arm.getInstance();
        
 
    }
    
	/**
	 * Before Auto Runs
	 */
    public void autonomousInit() {
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * Before Teleop runs
     */
    public void teleopInit(){
    	drivetrain.setState(DriveStates.TELEOP);
    	drivetrain.forceTeleop();
    	shooter.setAuto(true);
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

    	
    	 //intake
         if (left.getRawButton(1)) { 
        	intake.gimmeTehBall();
         } else if (left.getRawButton(2)) { 
        	intake.spitOut();
        	shooter.setFeederSpeed(-1);
         } else { 
        	 if(!shooting)
        		 shooter.setFeederSpeed(0);
        	intake.stopBoi();
        }
        
        //arm angles
        if(right.getRawButton(3)) {
        	arm.setSetpoint(FLOORPICK);
        } else if(right.getRawButton(4)) { 
        	arm.setSetpoint(CHIVALPICK);
        } else if(right.getRawButton(5)) {
        	arm.setSetpoint(STRAIGHTPICK);
        }
        
        //hood is set by button 5
        if(left.getRawButton(3)){
        	hood.setHood(SmartDashboard.getNumber("Hood Tuning",0));
        }
        
        //right thumb button turns to target
        if(right.getRawButton(2)) {
        	Turn turnNow = new Turn();
        	turnNow.startTurn(Vision.getTargetOffset());
        	drivetrain.setState(DriveStates.TELEOP);
        	drivetrain.forceTeleop();
        }
        
        //shooter
        if(right.getRawButton(1)) {
        	//trigger is outerworks shot
    		shooting = true;
        	hood.setHood(19);
    		shooter.setRPM(5500);
        	
        } else if(left.getRawButton(5)) {
        	//batter shot
    		shooting = true;
        	hood.setHood(4);
    		shooter.setRPM(5400);
        } else {
        	//stop shooting
        	shooting = false;
        	if(!firing){
    		Robot.shooter.setRPM(0);
        	}
        	
        }
        
	   shooter.setAuto(shooting);
		
       //if we see the target , start autoShooting
       if(Vision.seesTarget() && Vision.getDistance() <= 14) {
    	    //we set fire to true
    	    firing = true;
    	    
    	    //start shooting and dont shoot until the driver tells us to
        	shooter.setRPM(5000);
        	
        	//what time is it??
        	startTime = Timer.getFPGATimestamp();
        } else { 
        	//what time is it currently 
        	now = Timer.getFPGATimestamp();
        	
        	//how long has it been since weve seen a target
        	firing = (Math.abs(now - startTime) <= 10);
        	
        	//if nothings running and its been 5 seconds we stop shooting
        	shooter.setRPM( (shooting || firing) ? 5000 : 0 );
        	
        }
        

        //force the jelly to shoot
        if(right.getRawButton(6)){
        	//force the overrides and shoot
        	shooter.setFeederSpeed(1);
        } else {
        	//stop overriding
        	if(!shooting){
        		shooter.setFeederSpeed(0);
        	}
        }
        
        //preStart the batter shot
        if(operator.getRawButton(1)) {
        	hood.setHood(4);
        	shooter.setRPM(5400);
        } else {
        	if(!shooting && !firing)
        		shooter.setRPM(0);
        }
        
        //force the arm up or down
         if(operator.getRawButton(3)) {
        	arm.setMotorSpeed(-0.5);
        } else if(operator.getRawButton(4)) {
        	arm.setMotorSpeed(0.5);
        } else {
        	arm.setEnabled(true);
        }
        
         
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    
}
