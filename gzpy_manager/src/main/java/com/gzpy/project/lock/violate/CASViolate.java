package com.gzpy.project.lock.violate;

import java.util.concurrent.atomic.AtomicBoolean;

public class CASViolate {
	private volatile int count = 0;
	private  AtomicBoolean bl = new AtomicBoolean(true);
	public void increase() throws InterruptedException {
		while(getstate()) {
			count++;
			System.out.println("Thread :"+Thread.currentThread().getName()+"正在执行"+count);
			bl.compareAndSet(false, true);
			break;
		}
	}
	public boolean getstate() {
		System.out.println(Thread.currentThread().getName()+"state:"+bl.get());
		//CAS自旋
	    while(!bl.compareAndSet(true, false)) {
	    	
	    }
	    return true;
	}
	
	public static void main(String[] args) throws InterruptedException {
		CASViolate c = new CASViolate();
		for(int i=0;i<100;i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for(int i=0;i<100;i++) {
						try {
							c.increase();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			},"Thrad"+i).start();
		}
		System.out.println(c.count);
		
	}
}
