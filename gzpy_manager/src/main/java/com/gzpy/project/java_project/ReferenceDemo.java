package com.gzpy.project.java_project;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
/**
 * java引用类型
 * */
public class ReferenceDemo {
    public static void main(String[] args) {
    	//强引用
    	Object obj = new Object();
    	//释放
    	obj = null;
    	
    	//软引用 
    	//当对象只有软引用指向它，且内存紧张时释放，用于数据缓存
    	Object obj1 = new Object();
    	ReferenceQueue<Object> softQueue = new ReferenceQueue<Object>();
    	SoftReference<Object> softRef = new SoftReference<Object>(obj1,softQueue);
    	obj1 = null;
    	//弱引用
    	Object obj2 = new Object();
    	ReferenceQueue<Object> weakQueue = new ReferenceQueue<Object>();
    	WeakReference<Object> wr = new WeakReference<Object>(obj2,weakQueue);
    	obj2 = null;
    	//虚引用
    	/**
    	 * ReferenceQueue主要是用于监听Reference所指向的对象是否已经被垃圾回收。
    	 *     当SoftReference或WeakReference的get()加入ReferenceQueue或get()返回null时，
    	 *     仅是表明其指示的对象已经进入垃圾回收流程，此时对象不一定已经被垃圾回收。当PhantomReference
    	 *     加入ReferenceQueue时，则表明对象已经被回收。
    	 * */
    	Object obj3 = new Object();
    	ReferenceQueue<Object> PhantomQueen = new ReferenceQueue<Object>();
    	PhantomReference<Object> pr = new PhantomReference<Object>(obj3, PhantomQueen);
    	obj3 = null;
    	
    	while(true) {
    		Object r = null;
    		r = softQueue.poll();
    		if(r!=null) {
    			System.out.println("软引用被清理");
    		}
    		//1010
    		//  10001100
    		r = weakQueue.poll();
    		if(r!=null) {
    			System.out.println("弱引用被清理");
    		}
    		r = PhantomQueen.poll();
    		if(r!=null) {
    			System.out.println("虚引用被清理");
    		}
    		System.out.println("软引用："+softRef.get());
    		System.out.println("弱引用："+wr.get());
    		System.out.println("虚引用："+pr.get());
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
	}
}
