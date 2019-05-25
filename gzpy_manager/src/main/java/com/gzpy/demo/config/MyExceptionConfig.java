package com.gzpy.demo.config;

import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@Configuration
public class MyExceptionConfig {

	@Bean
	public ErrorAttributes getErrorAttributes() {
		return new DefaultErrorAttributes(true) {
			@Override
			public Map<String, Object> getErrorAttributes(WebRequest webRequest,
					boolean includeStackTrace) {
				Map map =  (Map) webRequest.getAttribute("ext", RequestAttributes.SCOPE_REQUEST);
				Map<String, Object> errorAttributes = 
						super.getErrorAttributes(webRequest, includeStackTrace);
				if(null!=map) {
					errorAttributes.putAll(map);
				}
				return errorAttributes;
			}
		};
	}
}