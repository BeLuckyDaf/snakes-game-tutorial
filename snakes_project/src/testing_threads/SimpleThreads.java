package testing_threads;

public class SimpleThreads {

	// TODO What does this method do?
	static void threadMessage(String message) {
		String threadName = Thread.currentThread().getName();
		System.out.format("%s: %s%n", threadName, message);
	}

	private static class MessageLoop implements Runnable {
		public void run() {
			String importantInfo[] = { "Mares eat oats", "Does eat oats", "Little lambs eat ivy",
					"A kid will eat ivy too" };
			try {
				// TODO What does this loop do?
				for (int i = 0; i < importantInfo.length; i++) {
					Thread.sleep(4000);
					threadMessage(importantInfo[i]);
				}
			} catch (InterruptedException e) {
				// TODO What does the program do if the thread is interrupted for any reason?
				threadMessage("I wasn't done!");
			}
		}
	}

	public static void main(String args[]) throws InterruptedException {

		// Time in milliseconds
		long patience = 1000 * 60 * 60;

		// If command line argument present, gives patience in seconds.
		if (args.length > 0) {
			try {
				patience = Long.parseLong(args[0]) * 1000;
			} catch (NumberFormatException e) {
				System.err.println("Argument must be an integer.");
				System.exit(1);
			}
		}

		threadMessage("Starting MessageLoop thread");
		// TODO Write why the startTime is collected
		long startTime = System.currentTimeMillis();
		// TODO What do the next two lines do? 
		Thread t = new Thread(new MessageLoop());
		t.start();

		threadMessage("Waiting for MessageLoop thread to finish");
		// TODO What's the stop criteria
		while (t.isAlive()) {
			threadMessage("Still waiting...");
			// TODO What is the purpose of the next line?
			t.join(1000);
			if (((System.currentTimeMillis() - startTime) > patience) && t.isAlive()) {
				threadMessage("Tired of waiting!");
				// TODO What does the next line do?
				t.interrupt();
				t.join();
			}
		}
		threadMessage("Finally!");
	}
}