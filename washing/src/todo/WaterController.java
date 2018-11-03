package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class WaterController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private WaterEvent waterEvent;
	private boolean filling;
	private boolean draining;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (2000/speed)); // TODO: replace with suitable period
		this.mach = mach;
	}

	public void perform() {
		// TODO: implement this method

		if (waterEvent != null) {
			WaterEvent checkWaterEvent = (WaterEvent) mailbox.tryFetch();
			if (checkWaterEvent != null) {
				waterEvent = checkWaterEvent;
			}
		} else {
			waterEvent = (WaterEvent) mailbox.doFetch();
		}

		if (waterEvent.getMode() == WaterEvent.WATER_IDLE) {
			mach.setDrain(false);
			draining = false;
			mach.setFill(false);
			filling = false;
			waterEvent = null;
		} else if (waterEvent.getMode() == WaterEvent.WATER_FILL) {
			if (!filling && mach.getWaterLevel() < waterEvent.getLevel()) {
				mach.setDrain(false);
				draining = false;
				mach.setFill(true);
				filling = true;
			} else if (mach.getWaterLevel() >= waterEvent.getLevel()) {
				mach.setFill(false);
				filling = false;
				((WashingProgram)waterEvent.getSource()).putEvent(new AckEvent(this));
				waterEvent = null;
			}
		} else if (waterEvent.getMode() == WaterEvent.WATER_DRAIN) {
			if (!draining) {
				mach.setHeating(false);
				mach.setFill(false);
				filling = false;
				mach.setDrain(true);
				draining = true;
			} else if (mach.getWaterLevel() == 0) {
				((WashingProgram)waterEvent.getSource()).putEvent(new AckEvent(this));
				waterEvent = null;
			}
		}
	}
}
