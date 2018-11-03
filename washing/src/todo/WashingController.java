package todo;

import done.*;

public class WashingController implements ButtonListener {	
    WashingProgram current = null;
    AbstractWashingMachine theMachine;
    double theSpeed;
    TemperatureController tempController;
    WaterController waterController;
    SpinController spinController;

    public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		this.theMachine = theMachine;
		this.theSpeed = theSpeed;
		tempController = new TemperatureController(theMachine, theSpeed);
		waterController = new WaterController(theMachine, theSpeed);
		spinController = new SpinController(theMachine, theSpeed);
		tempController.start();
		waterController.start();
		spinController.start();
    }

    public void processButton(int theButton) {
		switch (theButton) {
            case 0:
                if (current != null) {
                    current.interrupt();
                    current = null;
                }
                break;
            case 1:
                if (current == null) {
                    current = new WashingProgram1(theMachine, theSpeed, tempController, waterController, spinController);
                    current.start();
                }
                break;
            case 2:
                if (current == null) {
                    current = new WashingProgram2(theMachine, theSpeed, tempController, waterController, spinController);
                    current.start();
                }
                break;
            case 3:
                if (current == null) {
                    current = new WashingProgram3(theMachine,theSpeed,tempController,waterController,spinController);
                    current.start();
                }
                break;
        }
    }
}
