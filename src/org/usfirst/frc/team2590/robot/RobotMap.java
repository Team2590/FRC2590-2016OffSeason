package org.usfirst.frc.team2590.robot;

public interface RobotMap {
	//PWM Connections
    public static int PWM_driveRight = 5;
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
    
}
