package com.gzpy.demo.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gzpy.demo.po.User;
public class LoginInterceptor implements HandlerInterceptor{
	
	private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    		throws Exception {
    	HttpSession session = request.getSession();
    	Object user =  session.getAttribute("user");
    	if(user==null) {
    		log.info("请求路径{}",request.getRequestURL());
    		log.info("用户未登录");
    		request.getRequestDispatcher("/index.html").forward(request, response);
    		return false;
    	}
    	return true;
    }
}
