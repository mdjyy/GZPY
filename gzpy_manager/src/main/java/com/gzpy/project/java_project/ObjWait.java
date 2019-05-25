package com.gzpy.project.java_project;
/**
 * java.lang.IllegalMonitorStateException就是因为wait方法外的锁必须是wait对象的监听者
 * wait是释放锁等待唤醒
 * */
public class ObjWait {
    Object obj = new Object();
	public  void printNumber() {
			for(int i=0;i<26;i++) {
				System.out.println(i);
				try {
					this.notify();
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}

	public  void printChar() {
			for(int i=0;i<26;i++) {
				try {
					this.wait();
					this.notify();
					System.out.println((char)(i + 'A'));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
	}
		public static void main(String[] args) {
			ObjWait w = new ObjWait();
			Thread t1 = new Thread(new Runnable() {
			@Override
				public void run() {
				    synchronized (w) {
				    	w.printChar();
					}
				}	
			});
			Thread t2 = new Thread(new Runnable() {
				
				@Override
				public void run() {
					synchronized (w) {
						w.printNumber();					
					}
				}
			});
			
			t1.start();
			t2.start();
		}
	}
