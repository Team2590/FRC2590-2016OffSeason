package trajectory;

public class CC { //ControlConstants
    //Parameters
    
    
    //PID
    public static double kStraightP = 2.0;
    public static double kStraightI = 0;
    public static double kStraightD = 0;
    public static double kStraightV = 0.08;
    public static double kStraightA = 0.015;
    public static double kStraightTurn = 0.0016; //.08 //.007 //.0016
      
    public static double kPIDTurnVisionP = .01;
    public static double kPIDTurnVisionI = .006;
    public static double kPIDTurnVisionD = .006;
    
    public static double kPIDTurnP = .05;
    public static double kPIDTurnI = .00017;
    public static double kPIDTurnD = .006;
    
}
