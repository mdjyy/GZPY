package com.gzpy.project.lock;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class LockTest {
	public static int count = 0;
	public static Lock lock = new ZookeeperDistributeLock();
	public static CountDownLatch ctl = new CountDownLatch(50);
	public static int  getcount() {
		try {
			lock.lock();
			return ++count;
		} catch (Exception e) {
		}finally {
			lock.unlock();			
		}
		return 0;
	}
	@Test
	public  void a() {

		new Thread(new Runnable() {

			@Override
			public void run() {
                Lock lock = new ZookeeperDistributeLock();
                lock.lock();
                try {
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					lock.unlock();
				}
			}
		},"TestThread").start();

		for(int i=0;i<50;i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						ctl.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("count:"+LockTest.getcount());
				}
			},"Thread-"+i).start();
			ctl.countDown();
		}


		while(true) {

		}
	}
}
