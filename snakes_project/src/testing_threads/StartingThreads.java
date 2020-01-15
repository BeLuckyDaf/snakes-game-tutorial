package testing_threads;

public class StartingThreads {

	public static void main(String[] args) {
		// option1();
		option2();
	}

	private static void option1() {
		Thread t0 = new MailSystem(0);
		Thread t1 = new MailSystem(1);
		Thread t2 = new MailSystem(2);

		t0.start();
		t1.start();
		t2.start();

		System.out.println(Thread.activeCount());

		Thread[] listThreads = new Thread[Thread.activeCount()];
		Thread.enumerate(listThreads);
		for (Thread thread : listThreads) {
			System.out.println(thread.getName());
			System.out.println(thread.isAlive());
			System.out.println(thread.getStackTrace());
		}

		System.out.println("Main thread done");
	}
	
	private static void option2() {
		Thread t0 = new Thread(new MySystem());
		Thread t1 = new Thread(new MySystem());
		Thread t2 = new Thread(new MySystem());
		t0.start();
		t1.start();
		t2.start();
	}

}
