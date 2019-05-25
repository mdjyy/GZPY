package com.gzpy.project.java_project;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadLocalPoolName { 
	public static void main(String[] args) throws InterruptedException {
		
		ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newFixedThreadPool(8, Executors.defaultThreadFactory());
			for(int i=0;i<9;i++) {
				service.execute(new Runnable() {
					@Override
					public void run() {
						System.out.println(Thread.currentThread().getName()+"执行， 活跃线程："+service.getActiveCount());
					}	
				});
			}
		Thread.sleep(1000);
        System.out.println("taskcount"+service.getTaskCount()+",active:"+service.getActiveCount());
        for(int i=0;i<9;i++) {
			service.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName()+"执行， 活跃线程："+service.getActiveCount());
				}	
			});
		}
		//service.execute(new myRunnable(service));
	}
/**
 * 递归
 * */
	static class myRunnable implements Runnable{

		ExecutorService service;
		public myRunnable(ExecutorService service) {
			// TODO Auto-generated constructor stub
			this.service = service;
		}

		@Override
		public void run() { 
			System.out.println(Thread.currentThread().getName()+"hh");
			service.execute(new myRunnable(service));
		}
	}
}
