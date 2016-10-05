
package org.usfirst.frc.team2590.robot;

import auto.AutoModeTemplate;
import auto.ChivalDeAuto;
import auto.DoJackS;
import auto.GoOverDefenseAndShoot;
import auto.GoOverDefenseNoShot;
import auto.LowBar;
import auto.ShootAndPickup;
import auto.TwoBallLowBar;
import auto.TwoBallRoughTerrain;
import control.ShootingParameters;
import control.Vision;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import subsystems.Arm;
import subsystems.DriveTrain;
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
    private boolean shooting , firing; 
    private double startTime , now;
    
    private int autoType , autoPos;
    private AutoModeTemplate auto;
    
    //sensors
    private Joystick left , right , operator;
    private Encoder leftEnco , rightEnco;

    //subsystems and that stuff
    private static ShootingParameters outerWorks , batter;
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
    	
        leftEnco = new Encoder(2,3);
        rightEnco = new Encoder(1,0);
        leftEnco.setDistancePerPulse(1.0/360.0 * ((6.0 * Math.PI) / 12.0));
        rightEnco.setDistancePerPulse(1.0/360.0 * ((6.0 * Math.PI) / 12.0));
        
        operator = new Joystick(2);
        right = new Joystick(1);
        left = new Joystick(0);
        
        outerWorks = new ShootingParameters(14, 5400 , true);
        batter = new ShootingParameters(2, 5000 , true);
        shooting = false;
        firing = false;
        startTime = 0;
        now = 0;
        
        drivetrain = new DriveTrain(left, right ,leftEnco,rightEnco);
        shooter = new Shooter();
        intake = new Intake();
        hood = new Hood();
        arm = new Arm();
        
        autoType = (int) SmartDashboard.getNumber("Autonomous Mode");
        autoPos = (int) SmartDashboard.getNumber("Autonomous Position");
       
        //lowBar options
        if(autoType == 0){
        	//lowBar 1 shot
        	if(autoPos == 0){
        		auto = new LowBar();
        	//two ball in the lowBar
        	}else if(autoPos == 1){
        		auto = new TwoBallLowBar();
        	//drop ball , shoot , pick up second ball
        	}else{
        		auto = new ShootAndPickup();
        	}
         //Chival autos
        }else if(autoType == 1){
        	//go over chival and shoot
        	auto = new ChivalDeAuto();
        //Terrain autos
        }else if(autoType == 2){
        	if(autoPos == 0){
        		// go over the terrain but dont shoot
        		auto = new GoOverDefenseNoShot();
        	}else if(autoPos == 1){
        		//go over the terrain and shoot
        		auto = new GoOverDefenseAndShoot();
        	}else{
        		//two ball
        		auto = new TwoBallRoughTerrain();
        	}
        //do nothing
        }else{
        	auto = new DoJackS();
        }
    }
    
	/**
	 * Before Auto Runs
	 */
    public void autonomousInit() {
    	auto.run();
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
    	auto.cancel();
    	drivetrain.setAuto(false);
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
        	batter.reverseRollers();
         } else { 
        	 shooter.setAuto(shooting);
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
        	
        	drivetrain.setAuto(true);

        	while(!drivetrain.done())
        		drivetrain.turnToAngle(Vision.getTargetOffset());
        	
        	if(drivetrain.done()){
        		drivetrain.setAuto(false);
        	}
      
        }
        
        //shooter
        if(right.getRawButton(1)) {
        	//trigger is outerworks shot
        	outerWorks.setAuto(true);
        	outerWorks.start();
        	shooting = true;
        	
        } else if(left.getRawButton(5)) {
        	//batter shot
        	shooting = true;
    		batter.setAuto(true);
        	batter.start();

        } else {
        	//stop shooting
        	if(!firing){
        	shooting = false;
        	outerWorks.cancel();
        	batter.cancel();
        	}
        	
        }
        
       //if we see the target , start autoShooting
       if(Vision.seesTarget() && Vision.getDistance() <= 14) {
    	    //we set fire to true
    	    firing = true;
    	    
    	    //start shooting and dont shoot until the driver tells us to
        	//shooter.setRPM(5000);
        	batter.setAuto(shooting);
        	outerWorks.setAuto(shooting);
        	
        	//what time is it??
        	startTime = Timer.getFPGATimestamp();
        } else { 
        	//what time is it currently 
        	now = Timer.getFPGATimestamp();
        	
        	//how long has it been since weve seen a target
        	firing = (Math.abs(now - startTime) <= 10);
        	
        	//if nothings running and its been 5 seconds we stop shooting
        	shooter.setRPM( (shooting && firing) ? 5000 : 0 );
        	
        }
        

        //force the jelly to shoot
        if(right.getRawButton(6)){
        	//force the overrides and shoot
        	batter.startRollers();
        } else {
        	//stop overriding
        	batter.stopRoller();
        }
        
        //preStart the batter shot
        if(operator.getRawButton(1)) {
        	batter.setAuto(shooting);
        	batter.start();
        } else {
        	if(!shooting)
        		batter.cancel();
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
