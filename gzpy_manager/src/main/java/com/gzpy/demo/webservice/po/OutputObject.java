package com.gzpy.demo.webservice.po;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OutputObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OutputObject(){
		
	}
	//接口返回码
    private String retCode;
    //接口返回信息
    private String retMsg;
    //返回的javaben
    private List<Map<String, String>> beans = new ArrayList<Map<String, String>>();
    
    private Object bean;
    
    private List list = new ArrayList();

    public static OutputObject Builder() {
         return new OutputObject().buildRetCode("0").buildretMsg("调用成功");    	
    }
    
    public OutputObject buildRetCode(String retCode) {
    	this.retCode = retCode;
    	return this;
    }
    
    public OutputObject buildretMsg(String retMsg) {
    	this.retMsg = retMsg;
    	return this;
    }
    
    public OutputObject buildbeans(List<Map<String, String>> beans) {
    	this.beans = beans;
    	return this;
    }
    
    public OutputObject buildBean(Object bean) {
    	this.bean = bean;
    	return this;
    }
    
    public void addBeans(Map<String, String> map ) {
    	beans.add(map);
    }
    
    public String toJSON() {
    	ObjectMapper mapper = new ObjectMapper();
    	try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
    	return "序列化错误";
    }
    
    public OutputObject JsonToObject(String message) {
    	ObjectMapper mapper = new ObjectMapper();
    	try {
			return mapper.readValue(message, this.getClass());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return OutputObject.Builder().buildretMsg("反序列化错误").buildRetCode("9999");
    }
    
}
