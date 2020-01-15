package testing_threads;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestingSchedulling {

	public static void main(String[] args) {
		int corePoolSize = 1;
		ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(corePoolSize);
		pool.scheduleAtFixedRate(new MailSystem(1), 0, 2, TimeUnit.SECONDS);

	}

}
