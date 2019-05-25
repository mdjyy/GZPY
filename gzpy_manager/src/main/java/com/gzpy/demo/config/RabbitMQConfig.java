package com.gzpy.demo.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@PostConstruct
	public void init() {
		rabbitTemplate.setConfirmCallback(MyConfirmCallback());
	}

	
	
	/**
	 * 生产者发送消息确认
	 * spring:
          rabbitmq:
            publisher-confirms: true 
	 * */
	@Bean
	public ConfirmCallback MyConfirmCallback() {
		return new ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				System.out.println("消息唯一标识："+correlationData);
				System.out.println("确认结果："+ack);
				System.out.println("失败原因："+cause);
			}
		};
	}
	/**
	 * 消息序列化规则
	 * */
	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
}
