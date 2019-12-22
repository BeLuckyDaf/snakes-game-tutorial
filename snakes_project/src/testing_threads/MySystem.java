package testing_threads;

public class MySystem implements Runnable {

	@Override
	public void run() {
		try {
			Thread.sleep(4*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("System is done");
		
	}
}
