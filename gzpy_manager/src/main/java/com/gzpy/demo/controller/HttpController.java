package com.gzpy.demo.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HttpController {
	
	private static final Logger log = LoggerFactory.getLogger(HttpController.class);
	/**
	 * @param @requestBody注解，接收json对象，转换成map
	 * @param HttpServletResponse 响应
	 * #post请求内容放在请求体中，获取请求参数需要解析request.getInputStream()的字节,
	 * #对于springMvc中的注解@requestBody，首先解析请求内容类型是否为json,若是json
	 * #帮我们解析了输入流并封装到请求参数中，所以当有该注解的时候我们无法在解析这个输入流了
	 * */
	@RequestMapping("/html.do")
	public void getHTML(@RequestBody Map<String,String> params,HttpServletRequest request, HttpServletResponse res){
//		try {
//			InputStream in = request.getInputStream();
//			System.out.println(in.available());
//			DataInputStream dis = new DataInputStream(in);
//			byte b[] = new byte[10];
//			int len = -1;
//			String sb = "";
//			while((len=dis.read(b))!=-1) {
//				sb+=new String(b,0,len); 
//			}
//			log.info("请求体内容{},请求数{},请求内容类型{}",sb.toString(),sb.length(),request.getContentType());
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		log.info("入参{}",params);
		//创建一个客户端对象
		CloseableHttpClient client = HttpClients.createDefault();
		//HttpGet getRequest = new HttpGet("http://localhost:8080/redirection1/mobile?action=Index");
		HttpGet getRequest = new HttpGet("http://localhost:9999/gzpy/hello.do");
		CloseableHttpResponse response = null;
    	try {
    		//添加请求头
    		for(Entry<String,String> e:params.entrySet()) {
    			String key = e.getKey();
    			String value = e.getValue();
    			Header head = new BasicHeader(key,value);
    			//getRequest.addHeader(key, value);
    			getRequest.addHeader(head);
    		}
			response = client.execute(getRequest);
			HttpEntity entity = response.getEntity();
			//获取相应内容
			String ret = EntityUtils.toString(entity,"utf-8");
			log.info("获取响应状态:{}",response.getStatusLine());
			log.info("响应内容长度:{}",entity.getContentLength());
			log.info("响应内容:{}",ret);
			ret = replaceDemo(ret,"http://localhost:8080/redirection1");
			log.info("修改后响应内容:{}",ret);
			OutputStream out = res.getOutputStream();
			out.write(ret.getBytes());
    	} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(response!=null) {
					response.close();
				}
				if(client!=null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
	/**
	 * #替换文件中的域名
	 * */
	public String replaceDemo(String str,String Host) {
		//（）代表分组,第0组代表整个表达式，从左往右()为开始计数,
		//第2组为例如biz/js/indexw.js或者biz\\js\\indexw.js
    	Pattern p = Pattern.compile("(src|href)=\"((\\w+(/|\\\\))+(\\w|-|.)+.\\w+)\"");
		Matcher matcher = p.matcher(str);
		while(matcher.find()) {
			String resource = matcher.group(2);
			str = str.replaceAll(resource, Host+"/"+resource);
		}
		return str;
	}
	
	
    public static void main(String[] args) {
		HttpController c = new HttpController();
		//c.getHTML(null, null);
	}
}
