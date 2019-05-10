package com.gzpy.demo.controller;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.sl.usermodel.ObjectMetaData.Application;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
//@RestController
@Controller
public class DemoController {
    @RequestMapping("/hello.do")
    @ResponseBody
	public String HelloWorld(HttpServletRequest req){
    	Enumeration<String> e = req.getHeaderNames();
    	while(e.hasMoreElements()) {
    		System.out.println(e.nextElement());
    	}
    	int i = 1/0; 
    	return "Hello World";
    }
    @RequestMapping("/leaf.do")
    public String themLeaf(Map map) {
    	map.put("k1", "leaf green");
    	return "leaf";
    } 
}
