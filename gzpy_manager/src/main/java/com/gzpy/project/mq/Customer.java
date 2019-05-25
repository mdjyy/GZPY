package com.gzpy.project.mq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class Customer {
	public static final String QUEUE_NAME = "rabbit02_test";
	Object obj = new Object();
	public  void customer() {
		Connection conn = MQConnectionFactory.getConnection();
		try {
			Channel channel = conn.createChannel();
			channel.addShutdownListener(new ShutdownListener() {

				@Override
				public void shutdownCompleted(ShutdownSignalException cause) {
					System.out.println("channel断开"+cause.getMessage());

				}
			});
			channel.queueDeclare(QUEUE_NAME, true, false, false, null);
			System.out.println("Customer Waiting Received messages");
			//每次接受一个消息，在消息确认前不接受其他消息
			channel.basicQos(1);
			//false代表不自动确认
			channel.basicConsume(QUEUE_NAME,false,new MyCustomer(channel));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	class MyCustomer implements Consumer{

		private Channel channel;

		public MyCustomer(Channel channel) {
			this.channel = channel;
		}

		@Override
		public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
			System.out.println(Thread.currentThread().getName()+":handleShutdownSignal" );
		}

		@Override
		public void handleRecoverOk(String consumerTag) {
			System.out.println(Thread.currentThread().getName()+":handleRecoverOk" );

		}

		@Override
		public void handleDelivery(String consumerTag,
				Envelope envelope,
				AMQP.BasicProperties properties,
				byte[] body) throws IOException {
			try {
				//	System.out.println("handleDelivery "+"consumerTag:"+consumerTag+",envelope:"+envelope+",properties:"+properties+"message:"+new String(body,"utf-8"));
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				//BasicAck方法的第二个参数 multiple 取值为 false 时，表示通知 RabbitMQ 当前消息被确认；
				//如果为 true，则额外将比第一个参数指定的 delivery tag 小的消息一并确认。（批量确认针对的是整个信道）
				//delivery tag指的当前信道下消息的tag，consumerTag是channel信道的tag
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
			System.out.println(Thread.currentThread().getName()+":handleDelivery" +new String(body,"utf-8"));
		}

		@Override
		public void handleConsumeOk(String consumerTag) {
			System.out.println(Thread.currentThread().getName()+":handleConsumeOk" );

		}

		@Override
		public void handleCancelOk(String consumerTag) {
			System.out.println(Thread.currentThread().getName()+":handleCancelOk" );

		}

		@Override
		public void handleCancel(String consumerTag) throws IOException {
			System.out.println(Thread.currentThread().getName()+":handleCancel" );

		}

	}
	public  static void main(String[] args) throws InterruptedException {
		int count = 1;
		CountDownLatch ctl = new CountDownLatch(count);
		Customer c = new Customer();
		for(int i=0;i<count;i++) {
			new Thread(()->{
				try {
					ctl.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				c.customer();
			}).start();
			ctl.countDown();
		}
	}
}
