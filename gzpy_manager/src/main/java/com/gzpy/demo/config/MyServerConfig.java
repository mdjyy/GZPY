package com.gzpy.demo.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
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
