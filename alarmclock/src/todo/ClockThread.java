package todo;
import done.*;

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
			cs.increaseClockTime();
			int t = cs.getClockTime();
			output.showTime(t);
		}
	}
}
