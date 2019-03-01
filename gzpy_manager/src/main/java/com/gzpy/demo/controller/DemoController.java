package com.gzpy.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @RequestMapping("/hello.do")
	public String HelloWorld() {
    	return "Hello World";
    }
}
