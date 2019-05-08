package com.gzpy.project.lock;

import java.util.concurrent.CountDownLatch;

public class T {

	private static CountDownLatch ctl = new CountDownLatch(1);

	public static void t() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("挂起");
				try {
					ctl.await();
					System.out.println("唤醒");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}



	public static void main(String[] args) throws InterruptedException {
        T.t();
        Thread.sleep(100);
        ctl.countDown();
        //ctl = new CountDownLatch(1);
        System.out.println(1);
	}
}
