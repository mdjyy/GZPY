package com.gzpy.demo.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * controller控制器 Advice切面
 * 1、@ControllerAdvice注解将作用在所有注解了@RequestMapping的控制器的方法上
 * */
@ControllerAdvice
public class MyExceptionHandler {
    
	@ExceptionHandler(Exception.class)
	public String handlerException(Exception e,HttpServletRequest request) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code", "9999");
        map.put("exception",e);
        request.setAttribute("ext", map);
        request.setAttribute("javax.servlet.error.status_code",500);
		return "forward:/error";
    }
}
