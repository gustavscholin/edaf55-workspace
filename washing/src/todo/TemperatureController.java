package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class TemperatureController extends PeriodicThread {
	AbstractWashingMachine mach;
	double upperTempLimit;
	double lowerTempLimit;
	TemperatureEvent tempEvent = null;
	boolean warming;
	boolean inGoalInterval;



	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (10000/speed));
		this.mach = mach;
	}

	public void perform() {

		if (tempEvent != null) {
			TemperatureEvent checkTempEvent = (TemperatureEvent) mailbox.tryFetch();
			if (checkTempEvent != null) {
				tempEvent = checkTempEvent;
				inGoalInterval = false;
			}
		} else {
			tempEvent = (TemperatureEvent) mailbox.doFetch();
			inGoalInterval = false;
		}

		if (tempEvent.getMode() == TemperatureEvent.TEMP_IDLE) {
			mach.setHeating(false);
			warming = false;
			tempEvent = null;
		} else if (tempEvent.getMode() == TemperatureEvent.TEMP_SET) {
			upperTempLimit = tempEvent.getTemperature() - 0.7;
			lowerTempLimit = tempEvent.getTemperature() - 2 + 0.4;

			if (mach.getWaterLevel() > 0) {
				if (mach.getTemperature() < tempEvent.getTemperature() - 2) {
					inGoalInterval = false;
					mach.setHeating(true);
					warming = true;
				} else if (mach.getTemperature() > tempEvent.getTemperature()) {
					inGoalInterval = false;
					mach.setHeating(false);
					warming = false;
				} else if (!inGoalInterval) {
					inGoalInterval = true;
					((WashingProgram)tempEvent.getSource()).putEvent(new AckEvent(this));
				}

				if (inGoalInterval) {
					if (warming && mach.getTemperature() > upperTempLimit) {
						mach.setHeating(false);
						warming = false;
					} else if (!warming && mach.getTemperature() < lowerTempLimit) {
						mach.setHeating(true);
						warming = true;
					}
				}
			}
		}
	}
}
