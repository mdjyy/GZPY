package com.gzpy.demo.service;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.gzpy.demo.po.Employee;
import com.rabbitmq.client.Channel;

@Service
public class EmployeeAmqp {
	static int i = 0;
	@RabbitListener(queues= {"swwqueue","mdjqueue"})
	public void sendemployee(Employee e,Channel channel,Message message) {
		System.out.println(++i+":接受消息"+e);
		//手动确认
		//消费者确认消息
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * spring.rabbitmq.listener.simple.acknowledge-mode=manual手动确认
	 * */
	// @RabbitListener(queues="rabbit_test")
	public void test(String str ,Message message,Channel channel) {
		System.out.println("接受消息:"+message.getMessageProperties().getDeliveryTag()+":"+str);
		try {
			//消费者确认消息
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
