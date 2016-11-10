package org.usfirst.frc.team2590.robot;

public interface RobotMap {
	//PWM Connections
    public static int PWM_driveRight = 0;
    public static int PWM_driveLeft = 1;
    
    public static int PWM_intakeArticulate = 2;
    public static int PWM_intakeRollers = 7;
    public static int PWM_jelly = 3;
    
    public static int PWM_shooter = 6;
    public static int PWM_hood = 4;
    
    //Digital Connections
    public static int Digital_driveRightEncoderA = 0;
    public static int Digital_driveRightEncoderB = 1;
    public static int Digital_driveLeftEncoderA = 2;
    public static int Digital_driveLeftEncoderB = 3;
    public static int Digital_shooterEncoderA = 6;
    public static int Digital_shooterEncoderB = 7;
    
    //Analog Connections
    public static int Analog_IntakePos = 2;
    public static int Analog_HoodPos = 1;
    public static int Analog_Gyro = 0;
    
    public final double KP = 0.1; //0.1
	public final double KI = 0.006; //0.006
	public final double KD = 0.006;	//0.006
	
    public static double kStraightP = 2.0;
    public static double kStraightI = 0;
    public static double kStraightD = 0;
    public static double kStraightV = 0.08;
    public static double kStraightA = 0.015;
    public static double kStraightTurn = 0.0016; //.08 //.007 //.0016
}
