package com.gzpy.demo.config;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gzpy.demo.po.User;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MyServerConfig {
	
	@Bean
    public  ServletListenerRegistrationBean<HttpSessionListener> Listener(){
    	HttpSessionListener sessionListen = new HttpSessionListener() {
    		@Override
    		public void sessionCreated(HttpSessionEvent se) {
    			HttpSessionListener.super.sessionCreated(se);
    		}
    		
    		@Override
    		public void sessionDestroyed(HttpSessionEvent se) {
    			HttpSession session =  se.getSession();
    			Object user = session.getAttribute("myuser");
    	        log.info("用户注销{}",user);		
    			HttpSessionListener.super.sessionDestroyed(se);
    		}
    	};
    	ServletListenerRegistrationBean<HttpSessionListener> bean 
    	         = new ServletListenerRegistrationBean<HttpSessionListener>();
    	bean.setListener(sessionListen);
    	return bean;
    }
}
