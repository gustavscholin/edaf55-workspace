package queue;

import java.util.LinkedList;
import java.util.Queue;

class YourMonitor {
	private int nCounters;
	// Put your attributes here...
	private int lastArrived;
	private int lastServed;
	private Queue<Integer> freeClerks;

	YourMonitor(int n) { 
		nCounters = n;
		// Initialize your attributes here...
		freeClerks = new LinkedList<Integer>();
		for (int i = 0; i < n; i++) {
			freeClerks.offer(i);
		}
	}

	/**
	 * Return the next queue number in the intervall 0...99. 
	 * There is never more than 100 customers waiting.
	 */
	synchronized int customerArrived() { 
		// Implement this method...
		lastArrived = (lastArrived + 1) % 100;
		notifyAll();
		return lastArrived;
	}

	/**
	 * Register the clerk at counter id as free. Send a customer if any. 
	 */
	synchronized void clerkFree(int id) { 
		// Implement this method...
		if (!freeClerks.contains(id)) {
			freeClerks.offer(id);
			notifyAll();
		}
	}

	/**
	 * Wait for there to be a free clerk and a waiting customer, then
	 * return the cueue number of next customer to serve and the counter 
	 * number of the engaged clerk.
	 */
	synchronized DispData getDisplayData() throws InterruptedException { 
		// Implement this method...
		while (lastArrived == lastServed || freeClerks.isEmpty()) {
			wait();
		}
		lastServed = (lastServed + 1) % 100;
		int clerk = freeClerks.poll();

		return new DispData(lastServed, clerk);
	}
}
