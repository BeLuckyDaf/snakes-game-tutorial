package testing_threads;

import java.util.Date;

public class MailSystem extends Thread {

	private int id;

	// Add an static attribute to store the first to finish and which will be
	// updated only once.

	public MailSystem(int id) {
		this.id = id;
	}

	public void run() {
		// TODO Loop 20x, each iteration 2 seconds, print time
		for (int i = 0; i < 20; i++) {
			try {
				Thread.sleep(2 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.activeCount());
			System.out.println(id + " - " + new Date());
		}
	}
}
