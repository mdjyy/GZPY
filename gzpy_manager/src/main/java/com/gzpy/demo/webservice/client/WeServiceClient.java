package com.gzpy.demo.webservice.client;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzpy.demo.webservice.po.OutputObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeServiceClient {
    
	private static Client client;
	
	static {
		init();
	}
	public static void init() {
    	JaxWsDynamicClientFactory dcflient=JaxWsDynamicClientFactory.newInstance();
		client=dcflient.createClient("http://localhost:8989/ws/user?wsdl");
    }
	
	public static OutputObject execute(String methodName,Object... params) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("客户端调用webservice接口,入参:message={}",params);
		Object obj[] = null;
		try {
			obj = client.invoke(methodName, params);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("客户端调用webservice接口失败,入参:{}",params);
			return OutputObject.Builder().buildretMsg(e.getMessage()).buildRetCode("9999");
		}
		long endTime = System.currentTimeMillis();
		log.info("客户端调用webservice接口完成,入参:{}，用时{}ms",params,endTime-startTime);
		if(obj!=null) {
			String json = (String) obj[0];
			return OutputObject.Builder().JsonToObject(json);
		}
		return null;
	}
}
