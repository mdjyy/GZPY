package com.gzpy.project.lock;
/**
 * 锁的接口
 * */
public interface Lock {
    /**
     * 获取锁
     * */
	public void lock();
    /**
     * 释放锁
     * */
	public void unlock();
}
