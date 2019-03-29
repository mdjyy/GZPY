package com.gzpy.demo.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @RequestMapping("/hello.do")
	public String HelloWorld(HttpServletRequest req) {
    	Enumeration<String> e = req.getHeaderNames();
    	while(e.hasMoreElements()) {
    		System.out.println(e.nextElement());
    	}
    	return "Hello World";
    }
}
