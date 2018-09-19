package todo;
import done.*;
//import se.lth.cs.realtime.semaphore.Semaphore;
import se.lth.cs.realtime.semaphore.MutexSem;

public class ClockState {
	public int beepsToGo;
	private int clockTime;
	private int alarmTime;
	private boolean alarmOn;
	private MutexSem sem;
	private static ClockOutput	output;
	
	public ClockState(ClockOutput output) {
		beepsToGo = 0;
		clockTime = 0;
		alarmTime = 0;
		alarmOn = false;
		sem = new MutexSem();
		this.output = output;
	}
	
	public void setClockTime(int t) {
		sem.take();
		clockTime = t;
		sem.give();
	}
	
	public void setAlarmTime(int t) {
		sem.take();
		alarmTime = t;
		sem.give();
	}
	
	public void silence() {
		sem.take();
		beepsToGo = 0;
		sem.give();
	}
	
	public void toggleAlarm() {
		sem.take();
		alarmOn = !alarmOn;
		sem.give();
	}
	
	public int getClockTime() {
		return clockTime;
	}
	
	public void increase() {
		sem.take();
		clockTime++;
		if (clockTime % 100 == 60) {
			clockTime += 100;
			clockTime -= 60;
			if (clockTime % 10000 == 6000) {
				clockTime += 10000;
				clockTime -= 6000;
			}
		}
		if (alarmOn) {
			if (clockTime == alarmTime) {
				beepsToGo = 20;
			}
			if (beepsToGo > 0) {			
				output.doAlarm();
				beepsToGo--;
			}
		}
		sem.give();	
	}
}
