package com.gzpy.project.mq;

import java.io.IOException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQConnectionFactory {
	private static ConnectionFactory factory;
	static{
		factory = new ConnectionFactory();
		factory.setHost("192.168.0.100");
		factory.setPort(5675);
	}
	public static ConnectionFactory getMQFactory() {
		return factory;
	}
	
	public static Connection getConnection() {
		Connection conn = null;
	    try {
			conn = factory.newConnection();
			conn.addShutdownListener(new ShutdownListener() {
				@Override
				public void shutdownCompleted(ShutdownSignalException cause) {
					System.out.println("conn shutdown！！！！"+cause.getMessage());
				}
			});
		} catch (Exception e) {
		    log.info("创建连接失败,开始重试",e);
		    try {
				conn = factory.newConnection();
			}catch (Exception e1) {
                log.info("重试失败",e);
                return null;
			}
		}
	    return conn;
	}
	
	public static void close(Connection conn) {
		if(conn!=null) {
			try {
				conn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
