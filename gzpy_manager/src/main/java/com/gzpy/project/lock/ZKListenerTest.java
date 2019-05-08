package com.gzpy.project.lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * 测试同一个节点添加多个监听执行顺序
 * */
public class ZKListenerTest {
	private static final String CONNECTSTRING = "127.0.0.1:2181";
	public static ZkClient client = new ZkClient(CONNECTSTRING);
	protected static final String LOCK = "/lock1";
    /**
     * 结论：当注册多个监听器时，事件触发是按照顺序执行的，假如当第一个监听执行完毕后，节点又被注册上去了，后面的就不执行了
     * @throws InterruptedException 
     * */
	public static void main(String[] args) throws InterruptedException {
		client.delete(LOCK);
		//创建临时节点
		client.createEphemeral(LOCK);
		
		client.subscribeDataChanges(LOCK, new IZkDataListener() {
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
			    System.out.println("我是一号监听器");	
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				
			}
		});
		client.subscribeDataChanges(LOCK, new IZkDataListener() {
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
			    System.out.println("我是2号监听器");	
			    Thread.sleep(1000);
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				
			}
		});
		client.subscribeDataChanges(LOCK, new IZkDataListener() {
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
			    System.out.println("我是3号监听器");	
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				
			}
		});
		
		client.delete(LOCK);
		Thread.sleep(500);
		client.createEphemeral(LOCK);
		System.out.println("finish");
		while(true) {
			
		}
	}
}
