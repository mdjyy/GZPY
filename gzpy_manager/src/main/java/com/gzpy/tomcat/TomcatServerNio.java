package com.gzpy.tomcat;
/**
 * Nio Tomcat
 * */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomcatServerNio { 
	private static final Logger logger = LoggerFactory.getLogger(TomcatServerNio.class);
	//端口号   
	private int port;
	//自身实例
	private static TomcatServerNio tomcat;
	//server管道
	private ServerSocketChannel serverSocket;
	//选择器
	private Selector select;
	private static final Object lock = new Object();
	
	private TomcatServerNio(int port) {
		try {
			
			this.port = port;
			
			select = Selector.open();
			
			serverSocket = ServerSocketChannel.open();
			
			serverSocket.configureBlocking(false);
			
			SocketAddress address = new InetSocketAddress(this.port);
			
			serverSocket.bind(address) ;

		} catch (IOException e) {
			logger.error("初始化失败,端口号"+port,e);
		}
	}
	//启动
	public static void start(int port) {
		if(tomcat==null) {
			synchronized (lock) {
				if(tomcat==null) {
					tomcat = new TomcatServerNio(port);
				}
			}
		}
		tomcat.regist();
		tomcat.accept();
	}
	//服务注册到选择器上
	public void regist() {
		try {
			serverSocket.register(select,SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e) {
			logger.error("服务通道注册失败！");
		}
	}
	//接受客户端请求h
	public void accept() {
		new HandleThread().start();
	}

	class HandleThread extends Thread{ 
		private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
		public void handle() {
			while(true) {
				try {
					if(select.select()>0) {
						Set<SelectionKey> set = select.selectedKeys();
						logger.info("准备就绪事件:"+set.size());
						for(SelectionKey key : set) {
							logger.info("触发事件类型"+key.interestOps());
						}
						for(SelectionKey key : set) {
							//接受
							if(key.isAcceptable()) {
								ServerSocketChannel server = (ServerSocketChannel) key.channel();
								SocketChannel client = server.accept();
								client.configureBlocking(false);
								if(client.isConnected()) {
									logger.info("客户端已经连接");
									client.register(select, SelectionKey.OP_READ);
								}
								//获取事件为读取
							}else if(key.isReadable()) {
								SocketChannel client = (SocketChannel) key.channel();
								client.configureBlocking(false);
								doReadHandle(client);
							}
						}
						//一定要清除，不然会重复处理
						set.clear();
					}
				} catch (IOException e) {
					logger.error("I/O处理失败",e);
				}catch(Exception e) {
					logger.error("处理失败",e);
				}
			}
		}
		//处理客户端读取
		public void doReadHandle(SocketChannel client) {
			try {
				client.read(readBuffer);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(
								new ByteArrayInputStream(readBuffer.array())));
				//第一次读取时请求行第一行
				String line = in.readLine();
				logger.info("接受请求行第一行:"+line);
				//拆分http请求路径，取http需要请求的资源完整路径
				String resource = line.substring(line.indexOf('/'),line.lastIndexOf('/') - 5);
				logger.info("请求的资源:"+resource);

				resource = URLDecoder.decode(resource, "UTF-8");

				//获取到这次请求的方法类型，比如get或post请求
				String method = new StringTokenizer(line).nextElement().toString();
				logger.info("请求方法:"+method);

				while((line=in.readLine())!=null) {
					//当line等于空行的时候标志Header消息结束
					if ("".equals(line)) {
						break;
					}
					logger.info("请求头内容： "+line);
				}
				readBuffer.clear();
				send(client,method,resource);
			} catch (IOException e) {
				e.printStackTrace();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		//根据请求资源等发送给浏览器http
		public void send(SocketChannel client,String method,String resource) throws Exception{
			if("POST".equals(method)) {
				logger.debug("post请求，暂时不处理");
			}else if("GET".equals(method)){
				logger.info("get请求,请求资源："+resource);
				this.getClass().getClassLoader().getResource(resource);
				File file = new File(resource);
				if(file.exists()) {
					InputStream fis = new FileInputStream(file);
					StringBuffer sb = new StringBuffer();
					sb.append("HTTP/1.0 200 OK");
					sb.append("Content-Type:text/html;charset=utf-8");
					sb.append("\n");
					sendBuffer.put(sb.toString().getBytes());
					//将limit置为上次写的位置
					sendBuffer.flip();
					client.write(sendBuffer);
					//重置
					sendBuffer.reset();
					byte b[] = new byte[sendBuffer.capacity()];
					//读取的文件字节数
					int len = -1;
					while((len = fis.read(b))!=-1) {
						sendBuffer.put(b, 0, len);
						sendBuffer.flip();
						client.write(sendBuffer);
						sendBuffer.reset();
					}
					//以空格结尾
					sendBuffer.put("\n".getBytes());
					sendBuffer.flip();
					client.write(sendBuffer);
					fis.close();
				}else {
					StringBuffer sb = new StringBuffer();
					sb.append("HTTP/1.0 200 OK\n");
					sb.append("Content-Type:text/html;charset=utf-8\n");
					sb.append("\n\r");
					sb.append("<html><body>");
					sb.append("<b>HELLO WORLD123</b>");
					sb.append("</html></body>");
					sb.append("\r\n");
					sendBuffer.put(sb.toString().getBytes());
					sendBuffer.flip();
					client.write(sendBuffer);
					sendBuffer.clear();
				}
				//	client.register(select, SelectionKey.OP_CONNECT);
				client.close();
			}
		}

		@Override
		public void run() {
			handle();
		}
	}
	//启动
	public static void main(String[] args) {
		TomcatServerNio.start(8888);
	}
}
