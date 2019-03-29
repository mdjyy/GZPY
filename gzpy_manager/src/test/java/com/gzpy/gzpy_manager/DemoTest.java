package com.gzpy.gzpy_manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class DemoTest {
	public static void main(String[] args) {
		String str = "<script src=\"res/js/require-config.js\" ></script>";
		//    	Pattern p = Pattern.compile("src='((\\w+(/|\\\\))+\\w+.\\w+)'");
		Pattern p = Pattern.compile("(src|href)=\"((\\w+(/|\\\\))+(\\w|-)+.\\w+)\"");

		Matcher matcher = p.matcher(str);
		while(matcher.find()) {
			System.out.println(matcher.group(0));
			System.out.println("1"+matcher.group(2));
			str = str.replace(matcher.group(2),"localhost://appliction/"+matcher.group(2));
			// [^\\\?\/\*\|<>:"]  
			//"src='(\\w+\\|//)+.\\w"
		}
		System.out.println(str);
	}
	@Test
	public void t1() throws IOException {
		File temp = new File("p://l.txt");
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(temp));
		RandomAccessFile raf = new RandomAccessFile("C:\\Users\\mdj\\Desktop\\1.txt","rw");
		int hasReader = -1;
		byte []buff = new byte[64];
		raf.seek(1939);
		while((hasReader = raf.read(buff))>0){
			System.out.println("!!!!!!!!");
			bos.write(buff,0,hasReader);
		}
		raf.seek(1939);
		String content="qeqewqwe ";
		raf.write(content.getBytes());
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(temp));
		System.out.println("###");
		String str = "";
		while((hasReader = bis.read(buff)) > 0){
			raf.write(buff,0,hasReader);
			str+=new String(buff,0,hasReader);
		}		
		System.out.println("输出"+str);
		bos.close();
		bis.close();
		raf.close();
	}
}
