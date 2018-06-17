package auto;

import autoActions.DriveStraight;
import autoActions.FixIntake;
import autoActions.Turn;

public class ChivalDeAuto extends AutoModeTemplate {
	DriveStraight driveToChival;
	DriveStraight driveOverChival , driveToTower;
	FixIntake fix;
	Turn TurnAround;
	
	@Override
	public void init() {
		driveToChival = new DriveStraight(-(48/12), 8, 8);
		driveOverChival = new DriveStraight(-(90/12), 4, 8);
		driveToTower = new DriveStraight(-(48/12), 8, 8);
		fix = new FixIntake();
		TurnAround = new Turn();
	}

	@Override
	public void run() {
		driveToChival.run();
		waitTillDone(driveToChival.done());
		fix.downWithTheIntake();
		driveOverChival.run();
		waitTillDone(driveOverChival.done());
		driveToTower.run();
		waitTillDone(driveToTower.done());
		TurnAround.startTurn(180);

	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub

	}

}
