package todo;
import done.*;
import se.lth.cs.realtime.semaphore.Semaphore;

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
		int choice = 0;
		int lastChoice;
		while (true) {
			sem.take();
			cs.silence();
			lastChoice = choice;
			choice = input.getChoice();
			if (choice == input.SHOW_TIME && lastChoice == input.SET_ALARM) {
				cs.setAlarmTime(input.getValue());
			} else if (choice == input.SHOW_TIME && lastChoice == input.SET_TIME) {
				cs.setClockTime(input.getValue());
			}
			cs.setAlarm(input.getAlarmFlag());
		}
	}
}
