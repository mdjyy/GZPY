package com.gzpy.project.mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Producer {
    public static final String QUEUE_NAME = "rabbit_nginx_test";
	public static void produce() {
		Connection conn = MQConnectionFactory.getConnection();
        try {
			Channel channel = conn.createChannel();
			//为了防止生产者消息丢失，开启发送方确认模式
			channel.confirmSelect();
			//创建一个交换器
//			channel.exchangeDeclare("rabbit_exchange01", BuiltinExchangeType.FANOUT,true);
//			channel.queueDeclare(QUEUE_NAME+"_"+1, false,false,false,null);
//			channel.queueBind(QUEUE_NAME+"_"+1, "rabbit_exchange01", "test");
			//将deliverMode设置为2表示为消息持久化，1非持久化
			BasicProperties properties = new BasicProperties.Builder().deliveryMode(2).build();
			for(int i=0;i<20;i++) {
				channel.basicPublish("rabbit_exchange01", "",properties, ("这是第"+i+"条message").getBytes("utf-8"));
			}
			System.out.println("Producer Send");
			try {
				//等待服务器确认收到消息，确认后返回true
				if (channel.waitForConfirms()) {
				    System.out.println("消息发送成功" );
				}
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				//e.printStackTrace();
				//不成功这里可以重发或者怎么样
				//resend
			}
			channel.close();
			MQConnectionFactory.close(conn);
        } catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Producer.produce();
	}
}
