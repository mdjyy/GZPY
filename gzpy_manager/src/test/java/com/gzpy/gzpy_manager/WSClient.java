package com.gzpy.gzpy_manager;

import java.io.IOException;

import javax.jws.WebService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzpy.demo.webservice.client.WeServiceClient;
import com.gzpy.demo.webservice.po.OutputObject;
import com.gzpy.mdj.po.User;

public class WSClient {
	/**
	 * 调用方式1
	 * @throws Exception 
	 * */
	public static void main(String[] args) throws Exception {
//		JaxWsDynamicClientFactory dcflient=JaxWsDynamicClientFactory.newInstance();
//		Client client=dcflient.createClient("http://localhost:8989/ws/test?wsdl");
//		Object[] objects;
//		try {
//			objects = client.invoke("sayHello","wsclient");
//			System.out.println("*******"+objects[0].toString());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//http://localhost:8989/ws/user?wsdl
		User user = new User();
		user.setUserName("hhh");
		user.setUserId(21);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(user);
		System.out.println(WeServiceClient.execute("addUser", json));
	}
//	//调用方式2
//	public static void main2() {
//		JaxWsProxyFactoryBean jaxWsProxyFactoryBean=new JaxWsProxyFactoryBean();
//		jaxWsProxyFactoryBean.setAddress("http://localhost:8080/service/user?wsdl");
//		jaxWsProxyFactoryBean.setServiceClass(WebServiceTest.class);
//
//		UserService userService=(UserService)jaxWsProxyFactoryBean.create();
//		User userResult= userService.getUser("411001");
//		System.out.println("UserName:"+userResult.getUsername());
//		ArrayList<User> users=userService.getAlLUser();
//	}
//
//
//	//调用方式三，通过接口协议获取数据类型,设置链接超时和响应时间
//    public static void main3(String[] args) throws Exception {
//        JaxWsProxyFactoryBean jaxWsProxyFactoryBean=new JaxWsProxyFactoryBean();
//        jaxWsProxyFactoryBean.setAddress("http://localhost:8080/service/user?wsdl");
//        jaxWsProxyFactoryBean.setServiceClass(UserService.class);
//
//        UserService userService = (UserService) jaxWsProxyFactoryBean.create(); // 创建客户端对象
//        Client proxy= ClientProxy.getClient(userService);
//        HTTPConduit conduit=(HTTPConduit)proxy.getConduit();
//        HTTPClientPolicy policy=new HTTPClientPolicy();
//        policy.setConnectionTimeout(1000);
//        policy.setReceiveTimeout(1000);
//        conduit.setClient(policy);
//
//        User userResult= userService.getUser("411001");
//        System.out.println("UserName:"+userResult.getUsername());
//        ArrayList<User> users=userService.getAlLUser();
//
//    }
}
