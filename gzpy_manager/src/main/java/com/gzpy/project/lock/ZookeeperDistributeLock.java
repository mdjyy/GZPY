package com.gzpy.project.lock;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;

import lombok.extern.slf4j.Slf4j;
/**
 * 基于zk异常分布式锁,出现了死锁等待的现象,因为countdownlatch是共享资源 ，
 * 而每个线程在waitlock时都会new countdownlatch（1）,对于之前进行等待的线程也就是ctl.await方法。那时的ctl被改变了，
 * 线程信息也是被保存在之前的ctl对象中，对于注册的监听中的countdow方法，是无用的，因为ctl时new出来最新的，对于之前被改变的并不能够唤醒，所以会死锁
 * * 
 * */
@Slf4j
public class ZookeeperDistributeLock extends ZookeeperAbstractLock {

	/**
	 * 对于每个进程只有一个可以去获取锁，其他的都需要等待
	 * */
	//private CountDownLatch ctl = null;

	@Override
	public void unlock() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.delete(LOCK);
//		client.close();
		log.error(Thread.currentThread().getName()+"-------释放锁资源");
	}

	/**
	 * 创建临时节点LOCK成功，表示获取锁，如果存在该临时节点，
	 * 再次创建则会报错，返回false，表示获取锁失败
	 * *
	 */
	@Override
	public boolean trylock() {
		try {
			//Thread.sleep(1000);
			client.createEphemeral(LOCK);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void waitlock() {
        String threadName = Thread.currentThread().getName();
		MyZkDataListener listener = new MyZkDataListener(threadName);

		client.subscribeDataChanges(LOCK,listener);
		try {
			//有可能此时已经没有锁了，若还是执行await，会死锁，故要在尝试获取锁或者判断锁是否被占用
			//当然还有可能是判断是否存在时是有的，然后突然就被释放了，这个时候我们监听的会执行countdown，所以也是不会死锁的
			if(client.exists(LOCK)) {
				CountDownLatch ctl = listener.getCTL();
				log.error(Thread.currentThread().getName()+"-------被挂起，等待唤醒");
				ctl.await();
				log.error(Thread.currentThread().getName()+"-------等待结束");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		client.unsubscribeDataChanges(LOCK, listener);
	}
	
	private class MyZkDataListener implements IZkDataListener{
		private CountDownLatch ctl = new CountDownLatch(1);
        private String threadName; 
        
		public MyZkDataListener(String name) {
		    this.threadName = name;
		}

		@Override
		public void handleDataDeleted(String dataPath) throws Exception {
		        System.out.println(threadName+":count--");	
				ctl.countDown();
		}

		@Override
		public void handleDataChange(String dataPath, Object data) throws Exception {

		}
		
		public CountDownLatch getCTL() {
			return ctl;
		}
	}
}
