package com.gzpy.demo.webservice.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.cxf.endpoint.Client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProxyWSClient implements InvocationHandler{

	private Client client;
	
	public ProxyWSClient(Client ws) {
	    this.client = ws;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long startTime = System.currentTimeMillis();
		log.info("客户端调用webservice接口,方法{},入参:message={}",method.getName(),args);
		Object obj[] = null;
		try {
			obj = (Object[]) method.invoke(client, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		log.info("客户端调用webservice接口完成,方法{},入参:{}，用时{}ms",method.getName(),args,endTime-startTime);
		return obj[0];
	}
	
	public Client getProxy() {
		return (Client) Proxy.newProxyInstance(client.getClass().getClassLoader(),new Class[]{Client.class},this);
	}
    
}
