
package org.usfirst.frc.team2590.robot;

import auto.LowBar;
import auto.RoughTerrain;
import auto.chivalDeAuto;
import autoActions.Turn;
import control.Vision;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Scheduler;
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
    Encoder leftE , rightE;
    
    //other variables
    private boolean shooting = false; 
    private boolean opShooting = false;
    private boolean forceShooting = false;
  
    //sensors
    private Joystick left , right , operator;

    //subsystems and that stuff

    public static DriveTrain drivetrain;
    public static Shooter shooter;
    public static Intake intake;
    public static Hood hood;
    public static Arm arm;
    
    //autos
    private static LowBar low;
    private static chivalDeAuto ch;
    private static RoughTerrain t;
    
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	//joysticks
        operator = new Joystick(2);
        right = new Joystick(1);
        left = new Joystick(0);
        low = new LowBar();
        ch =new chivalDeAuto();
        t = new RoughTerrain();
        
        leftE = new Encoder(0,1); //32
        rightE = new Encoder(3,2); //01


        rightE.setDistancePerPulse(1.0/360.0 * ((6.0 * Math.PI) / 12.0));
        leftE.setDistancePerPulse(1.0/360.0 * ((6.0 * Math.PI) / 12.0));
        
        //implements a singleton design pattern to assure that only one
        //class uses a class and its motors at one time
        drivetrain = new DriveTrain(left, right , leftE , rightE);
        shooter = Shooter.getInstance();
        intake = Intake.getInstance();
        hood = Hood.getInstance();
        arm = Arm.getInstance();
 
    }
    
	/**
	 * Before Auto Runs
	 */
    public void autonomousInit() {
        t.init();
    	t.run();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * Before Teleop runs
     */
    public void teleopInit() {
    	drivetrain.setState(DriveStates.TELEOP);
    	drivetrain.forceTeleop();
    	shooter.setAuto(true);
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
        Scheduler.getInstance().run();
    	
    	 //intake
         if (left.getRawButton(1)) { 
        	intake.gimmeTehBall();
        	arm.setSetpoint(0);
        	
         } else if (left.getRawButton(2)) { 
        	intake.spitOut();
        	shooter.setFeederSpeed(-1);
        	forceShooting = true;
         } else { 
        	forceShooting = false;
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
        
        //hood is set by button 3
        if(left.getRawButton(3)) {
        	hood.setHood(SmartDashboard.getNumber("Hood Tuning",0));
        }
        
        //right thumb button turns to target
        if(right.getRawButton(2)) {
        	Turn turnNow = new Turn();
        	turnNow.startTurn(Vision.getTargetOffset());
        	
        	if(turnNow.finished()){
        	drivetrain.setState(DriveStates.TELEOP);
        	drivetrain.forceTeleop();
        	
        	}
        }
        
        
        /*
         *shooting controls 
         */
        if(right.getRawButton(1)) {
        	//trigger is outerworks shot
    		shooting = true;
        	hood.setHood(20);
    		shooter.setRPM(5600);
        	
        } else if(left.getRawButton(5)) {
        	//batter shot
    		shooting = true;
    		opShooting = false;
        	hood.setHood(5);
    		shooter.setRPM(5000);
    		
        } else {
        	//stop shooting
        	shooting = false;
        	
        }
        
        //if the operator isn't shooting and the driver isnt shooting , then stop shooting
        if(!shooting && !opShooting) {
    		Robot.shooter.setRPM(0);
        }
        
        if(!shooting) {
        	Robot.shooter.setFeederSpeed(0);
        }
         
        //if the driver is trying to shoot, set auto to true , but if only the operator is shooting then dont auto
	   shooter.setAuto((shooting && !opShooting) && !forceShooting);
	
	   
	   	/*
	   	 * Operator Controls , 3rd joystick 
	   	 */
	  
        //preStart the batter shot
        if(operator.getRawButton(1)) {
        	opShooting = !shooting;
        	hood.setHood(5);
        	shooter.setRPM(5400);
        	
        } else {
        	opShooting = false;
        	
        }
        
        //force the arm up or down
         if(operator.getRawButton(3)) {
        	arm.setMotorSpeed(-0.5);
        	
        } else if(operator.getRawButton(4)) {
        	arm.setMotorSpeed(0.5);
        	
        }
         
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    public void disabledPeriodic(){
    	Robot.drivetrain.gyro.reset();
    	Robot.drivetrain.leftE.reset();
    	Robot.drivetrain.rightE.reset();
    }
    
}
