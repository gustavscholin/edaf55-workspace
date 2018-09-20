package todo;
import done.*;
import se.lth.cs.realtime.semaphore.Semaphore;
import se.lth.cs.realtime.semaphore.MutexSem;

public class AlarmClock extends Thread {

	private static ClockInput	input;
	private static ClockOutput	output;
	private static Semaphore	sem;
	private static ClockState	cs;

	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		sem = input.getSemaphoreInstance();
		cs = new ClockState(output);

	}

	// The AlarmClock thread is started by the simulator. No
	// need to start it by yourself, if you do you will get
	// an IllegalThreadStateException. The implementation
	// below is a simple alarmclock thread that beeps upon 
	// each keypress. To be modified in the lab.
	public void run() {
//		while (true) {
//			sem.take();
//			System.out.println(input.getChoice());
//			System.out.println(input.getValue());
//			output.doAlarm();
//		}
		ClockThread ct = new ClockThread(cs, output);
		ButtonThread bt = new ButtonThread(cs, input, output);
		ct.start();
		bt.start();
	}
}
