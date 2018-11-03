package todo;


import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;


public class SpinController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private SpinEvent spinEvent;
	private double timeNow;
	private int spinDir = AbstractWashingMachine.SPIN_LEFT;
	private double changeDirTime;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed));
		this.mach = mach;
		changeDirTime = 60000/speed;
	}

	public void perform() {
		if (spinEvent != null) {
			SpinEvent checkSpinEvent = (SpinEvent) mailbox.tryFetch();
			if (checkSpinEvent != null) {
				spinEvent = checkSpinEvent;
				timeNow = System.currentTimeMillis();
			}
		} else {
			spinEvent = (SpinEvent) mailbox.doFetch();
			timeNow = System.currentTimeMillis();
		}

		if (spinEvent.getMode() == SpinEvent.SPIN_OFF) {
			mach.setSpin(AbstractWashingMachine.SPIN_OFF);
			spinEvent = null;
		} else if (spinEvent.getMode() == SpinEvent.SPIN_SLOW) {
			if (System.currentTimeMillis() > timeNow + changeDirTime) {
				if (spinDir == AbstractWashingMachine.SPIN_LEFT) {
					spinDir = AbstractWashingMachine.SPIN_RIGHT;
				} else {
					spinDir = AbstractWashingMachine.SPIN_LEFT;
				}
				timeNow = System.currentTimeMillis();
				mach.setSpin(spinDir);
			}
		} else if (spinEvent.getMode() == SpinEvent.SPIN_FAST) {
			if (mach.getWaterLevel() == 0) {
				mach.setSpin(AbstractWashingMachine.SPIN_FAST);
				spinEvent = null;
			}
		}
	}
}
