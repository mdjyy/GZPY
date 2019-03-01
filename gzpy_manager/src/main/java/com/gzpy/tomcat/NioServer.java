package com.gzpy.tomcat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    /*��������С*/ 
    private  int BLOCK = 1024;  
    
    private  Selector selector;  
    
    public NioServer(int port) throws IOException{
    	// �򿪷������׽���ͨ��  
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();  
        // ����������Ϊ������  
        serverSocketChannel.configureBlocking(false);  
        // �������ͨ�������ķ������׽���  
        ServerSocket serverSocket = serverSocketChannel.socket();  
        // ���з���İ�  
        serverSocket.bind(new InetSocketAddress(port));  
        // ͨ��open()�����ҵ�Selector  
        selector = Selector.open();  
        // ע�ᵽselector���ȴ�����  
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  
        System.out.println("Server Start----8888:");  
    }
    // ����  
    private void listen() throws IOException {  
    	while (true) {  
            selector.select();  
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println(selectionKeys.size());
            Iterator<SelectionKey> iterator = selectionKeys.iterator();  
            while (iterator.hasNext()) {          
                SelectionKey selectionKey = iterator.next();  
                iterator.remove();
                handleKey(selectionKey);  
            }  
        }  
    }  
    
    // ��������  
    private void handleKey(SelectionKey selectionKey) throws IOException {  
        // ���Դ˼���ͨ���Ƿ���׼���ý����µ��׽������ӡ�  
        if (selectionKey.isAcceptable()) {
        	accept(selectionKey);
        } else if (selectionKey.isReadable()) {
        	receive(selectionKey);
        } else if (selectionKey.isWritable()) {  
            send(selectionKey);
        }  
    }  
    
    public void accept(SelectionKey selectionKey) throws IOException{
    	  // ��������  
        ServerSocketChannel server = null;  
        SocketChannel client = null;
        // ����Ϊ֮�����˼���ͨ����  
        server = (ServerSocketChannel) selectionKey.channel();  
        // ���ܵ���ͨ���׽��ֵ����ӡ�  
        // �˷������ص��׽���ͨ��������У�����������ģʽ��  
        client = server.accept();  
        // ����Ϊ������  
        client.configureBlocking(false);  
        // ע�ᵽselector���ȴ�����  
        client.register(selector, SelectionKey.OP_READ);
    }
    
    public void receive(SelectionKey selectionKey) throws IOException{
        /*�������ݻ�����*/ 
        ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);  
    	// ��������  
        SocketChannel client = null;  
        String receiveText = "";  
        int count=0;  
        // ����Ϊ֮�����˼���ͨ����  
        client = (SocketChannel) selectionKey.channel();  
        //������������Ա��´ζ�ȡ  
        receivebuffer.clear();  
        //��ȡ�����������������ݵ���������  
		/*
		 * do{ count = client.read(receivebuffer); receiveText = receiveText + new
		 * String( receivebuffer.array(),0,count); }while(count>0); int index = 0;
		 * if((index = receiveText.indexOf("open_"))!=-1){ String name =
		 * receiveText.substring(index+1); selectionKey.attach(name); }
		 */
        client.read(receivebuffer);
        System.out.println(new String(receivebuffer.array()));
        //client.register(selector, SelectionKey.OP_WRITE);  
    }
    
    public void send(SelectionKey selectionKey) throws IOException{
        ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);  
    	 // ��������  
        SocketChannel client = null;  
        String sendText;  
    	 //������������Ա��´�д��  
        sendbuffer.clear();  
        // ����Ϊ֮�����˼���ͨ����  
        client = (SocketChannel) selectionKey.channel();  
        sendText="message from server--" ;
        //�򻺳�������������  
        sendbuffer.put(sendText.getBytes());  
         //������������־��λ,��Ϊ������put�����ݱ�־���ı�Ҫ����ж�ȡ���ݷ��������,��Ҫ��λ  
        sendbuffer.flip();  
        //�����ͨ��  
        client.write(sendbuffer);  
        System.out.println("����������ͻ��˷�������--��"+sendText);  
        client.register(selector, SelectionKey.OP_READ);  
    }
    
    /**  
     * @param args  
     * @throws IOException  
     */ 
    public static void main(String[] args) throws IOException {  
        // TODO Auto-generated method stub  
        int port = 8888;  
        NioServer server = new NioServer(port);  
        server.listen();  
    }  
}
