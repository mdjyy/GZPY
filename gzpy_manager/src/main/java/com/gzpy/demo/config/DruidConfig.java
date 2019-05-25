package com.gzpy.demo.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
public class DruidConfig {

	@ConfigurationProperties("spring.datasource")
	@Bean
	public DataSource druid() {
		return new DruidDataSource();
	}

	@Bean
	public ServletRegistrationBean<Servlet> StatViewServlet(){
		ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
		Map initParams = new HashMap();
		initParams.put("loginUsername","admin");
		initParams.put("loginPassword","123456");
		initParams.put("allow","");//默认就是允许所有访问
		initParams.put("deny","192.168.15.21");
		bean.setInitParameters(initParams);
		return bean;
	}
	@Bean
	public FilterRegistrationBean webStatFilter(){
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new WebStatFilter());

		Map<String,String> initParams = new HashMap<>();
		initParams.put("exclusions","*.js,*.css,/druid/*");

		bean.setInitParameters(initParams);

		bean.setUrlPatterns(Arrays.asList("/*"));
		return  bean;
	}
}
