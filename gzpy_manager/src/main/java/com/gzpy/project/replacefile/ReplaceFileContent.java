package com.gzpy.project.replacefile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 当读写操作同一个文件时，写流没有关闭，读取流读取的是一个之前的副本，所以要关闭
 * 副本跟追加写入有关
 * **/
public class ReplaceFileContent {
	private static final Logger log = LoggerFactory.getLogger(ReplaceFileContent.class);

	public static void ReplaceFile(File file) throws Exception {
		//raf
		//new ChangeByRAF().doHandle(file);
		new ChangeByFC().doHandle(file);
	}

	public static void main(String[] args) throws Exception {
//				String rep = "'jquery': 'jquery',";
//				String s = rep.split(":")[1];
//			    System.out.println(s.substring(s.indexOf("'")+1));
//			    StringTokenizer stringTokenizer = new StringTokenizer(rep,"'");
//			    stringTokenizer.hasMoreTokens();
//			    System.out.println(stringTokenizer.nextElement());
//		String line = "'domReady' : 'base/require-domReady',";
//		//'.*:.*('\\w+')
//		Pattern p = Pattern.compile("'\\w+'.{0,2}:.{0,2}'(.+'.{0,2})");
//		Matcher m = p.matcher(line);
//		System.out.println(m.find());
//		System.out.println(m.group());
//		System.out.println(m.group(1));
		new ReplaceFileContent().ReplaceFile(new File("C:\\Users\\mdj\\Desktop\\1.txt"));
	}

	/**
	 * 通过RandomAccessFile改变内容
	 * */
	private static class ChangeByRAF implements ChangeFileContent{

		@Override
		public void doHandle(File file) throws Exception {
			RandomAccessFile raf = null;
			StringBuffer sb = new StringBuffer();
			BufferedInputStream bis =null;
			BufferedOutputStream bos= null;
			//临时文件
			File temp = File.createTempFile("temp",null);
			temp.createNewFile();
			temp.deleteOnExit();
			try {
				long lastpoint = 0L;
				raf = new RandomAccessFile(file,"rw");
				String line = null;
				byte b[] = new byte[64];
				while((line=raf.readLine())!=null) {
					//获取当前读取的指针
					final long point = raf.getFilePointer();
					Pattern p = Pattern.compile("'\\w+'.{0,2}:.{0,2}'(.+'.{0,1})");
                    Matcher matcher = p.matcher(line);
                    String oldStr = "";
					if(matcher.find()) {
						//当需要写入时先将后面的内容先保存到临时文件中防止覆盖
						int len = -1;
						//每次都新建一个写入流
						bos = new BufferedOutputStream(new FileOutputStream(temp,false));
						bis = new BufferedInputStream(new FileInputStream(temp));
						bos.write("\r\n".getBytes());
						while((len=raf.read(b))!=-1) {
							bos.write(b,0,len);
						}
						bos.close();
						oldStr = matcher.group(1);
						sb.append(line).replace(line.indexOf(":")+1,line.length(),"http://localhost:8080/redirection1"+oldStr);
						log.info("处理一条信息{}",sb.toString());
						//将指针移动到写入的位置,将修改后的插入
						raf.seek(lastpoint);
						raf.write(sb.toString().getBytes());
						//获取下次读取位置
						long toread = raf.getFilePointer();
						//将之前复制到临时文件写回，指针从上次写入的地方追加
						String reWrite = "";
						while((len=bis.read(b))!=-1) {
							raf.write(b, 0, len);
							reWrite+=new String(b,0,len);
						}
						log.info("reWrite{}",reWrite);
						raf.seek(toread);
					}
					//追加玩指针应该指向末尾，要从上次插入的位置在开始读
                    lastpoint = point;
                    sb.delete(0, sb.length());
				}
			}  catch(Exception e) {
				e.printStackTrace();
			}finally {
				bis.close();
				raf.close();
			}
		}

	} 
	/**
	 * FileChannel 
	 * */
	private static class  ChangeByFC implements ChangeFileContent {
		private FileChannel channel = null;
		
		private ByteBuffer bf = null;
		
		private StringBuffer sb ; 
		
		public  ChangeByFC() {
           bf = ByteBuffer.allocate(1024);
           sb = new StringBuffer();
		}
		@Override
		public void doHandle(File file) throws Exception {
		    //文件通道读写
			channel = FileChannel.open(Paths.get(file.getPath()),StandardOpenOption.WRITE,StandardOpenOption.READ);
			int len = -1;
			while((len = channel.read(bf))!=-1) {
				sb.append(new String(bf.array(),0,len));
				bf.clear();
			}
			Pattern p = Pattern.compile("'\\w+'.{0,2}:.{0,2}'(.+'.{0,1})");
			Matcher matcher = p.matcher(sb.toString());
			
			String ret = sb.toString();
			
			while(matcher.find()) {
				String g = matcher.group();//'domReady' : 'base/require-domReady',
				String oldStr = matcher.group(1);//base/require-domReady',
				ret = ret.replace(oldStr,"http://127.0.0.1:8080/redirection1/biz/js/"+oldStr+"\r\n");
			}
			log.info("修改完成{}",ret);
			bf.clear();
//			//指针复0
			channel.position(0);
			byte b[] = ret.toString().getBytes();
			for(int j =0;j<b.length;j++) {
				bf.put(b[j]);
				bf.flip();
				channel.write(bf);
				bf.clear();
			}
			channel.close();
		}
	}
	
	
	private static interface ChangeFileContent {
		public void doHandle(File file) throws Exception;
	}
}
