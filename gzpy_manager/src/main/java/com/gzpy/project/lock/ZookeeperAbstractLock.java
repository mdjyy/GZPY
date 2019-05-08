package com.gzpy.project.lock;

import org.I0Itec.zkclient.ZkClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ZookeeperAbstractLock implements Lock{
	
	//private static final String CONNECTSTRING = "127.0.0.1:2181";
	private static final String CONNECTSTRING = "192.168.164.128";
	protected ZkClient client = new ZkClient(CONNECTSTRING);
	protected static final String LOCK = "/lock";
	
	@Override
    public void lock() {
        if(trylock()) {
            log.error(Thread.currentThread().getName()+"------获取锁");
        }else {
        	waitlock();
        	
        	lock();
        }	
    }
	
	public abstract boolean trylock();
	
	public abstract void waitlock();
	
}
