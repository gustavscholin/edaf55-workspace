package todo;
import done.*;
import se.lth.cs.realtime.semaphore.Semaphore;
import se.lth.cs.realtime.semaphore.MutexSem;

public class ButtonThread extends Thread {
	private static ClockState cs;
	private static ClockInput input;
	private static ClockOutput output;
	private static Semaphore	sem;
	
	public ButtonThread(ClockState cs, ClockInput input, ClockOutput output) {
		this.cs = cs;
		this.output = output;
		this.input = input;
		sem = input.getSemaphoreInstance();
	}
	
	public void run() {	
		while (true) {
			sem.take();
			cs.silence();
			int choice = input.getChoice();
			if (choice == input.SET_ALARM) {
				cs.setAlarmTime(input.getValue());
			} else if (choice == input.SET_TIME) {
				cs.setClockTime(input.getValue());
			}
		}
	}
}
