package todo;
import done.*;
import se.lth.cs.realtime.semaphore.Semaphore;
import se.lth.cs.realtime.semaphore.MutexSem;

public class ClockThread extends Thread {
	private static ClockState cs;
	private static ClockOutput output;
	
	public ClockThread(ClockState cs, ClockOutput output) {
		this.cs = cs;
		this.output = output;
	}
	
	public void run() {
		long currentTime = System.currentTimeMillis();
		while (true) {
			currentTime += 1000;
			long now = System.currentTimeMillis();
			long dt = currentTime - now;
			if (dt > 0) {
				try { Thread.sleep(dt); } catch (InterruptedException e) { }			
			}
			cs.increase();
			int t = cs.getClockTime();
			output.showTime(t);
		}
	}
}
