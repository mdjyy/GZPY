package com.gzpy.demo.config;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.support.AbstractFlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import org.springframework.web.util.WebUtils;

import com.gzpy.demo.i18n.MyLocalResolve;

import lombok.extern.slf4j.Slf4j;
/**
 * 自jdk1.8后，接口增加了默认实现的方法类似于如下
 * default void configurePathMatch(PathMatchConfigurer configurer) {}
 * 所以我们再写配置mvc配置可以不需要适配器WebMvcConfigurerAdapter，直接实现WebMvcConfigurer
 * 即可
 * 若不需要boot帮我们自动配置springmvc，在类上添加@@EnableWebMvc，自己去写配置文件配置mvc
 * */
@Configuration
@Slf4j
public class MyMvcConfifg  implements WebMvcConfigurer{
	/**
	 * 浏览器发送view请求，视图解析器去head页面
	 * */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// TODO Auto-generated method stub
		//registry.addViewController("/view").setViewName("Head");
		//registry.addViewController("/hello.do").setViewName("test");
		registry.addViewController("/index.html").setViewName("login");
		registry.addViewController("/").setViewName("login");
		registry.addViewController("/Head").setViewName("Head");
		registry.addViewController("/login").setViewName("login");

	}
	/**
	 * 因为boot会默认将容器中的WebMvcConfigurer同时生效，我们还可以这样
	 * */
	@Bean
	public WebMvcConfigurer WebMvcConfigurer(){
		return  new WebMvcConfigurer() {
			/*
			 * @Override public void addViewControllers(ViewControllerRegistry registry) {
			 * registry.addViewController("/mdj.do").setViewName("leaf");
			 * registry.addViewController("/index.html").setViewName("login"); }
			 */
			 @Override
			public void addInterceptors(InterceptorRegistry registry) {
				 HandlerInterceptor c = new HandlerInterceptor() {
				    /**
				     * 1、在请求controller之前执行，返回true继续执行，false当前请求结束，先声明先执行
				     * */
					 @Override
					public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
							throws Exception {
						return HandlerInterceptor.super.preHandle(request, response, handler);
					}
					/**
					 * 2、在controller之后执行，但是会在DispatcherServlet视图渲染之前执行Controller 
					 * ,主要对处理之后的ModelAndView 对象进行操作,绑定数据给页面，先声明的后执行
					 * */ 
					@Override
				    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
				    		ModelAndView modelAndView) throws Exception {
				    	 System.out.println("拦截");
				    }
					/**
					 * 3、是在DispatcherServlet 渲染了对应的视图之后执行。这个方法的主要作用是用于进行资源清理工作的。
					 * */
					@Override
					public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
							Object handler, Exception ex) throws Exception {
						HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
					}
				 };
				 /**
				  * 1、在2.0以上的boot自定义拦截器会拦截静态资源
				  * */
				 registry.addInterceptor(c).addPathPatterns("/mdj.do"); 
				 String excludePath[] = {"/**/**.js","/login.do","/","/index.html","/**/**.css","/demo/asserts/**","/webjars/**","/user/login"};
				 registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns(excludePath);
			 }
			 
		};
	}
	/**
	 *1、在WebMVC自动配置中有如下代码，当我们自定义了一个 LocaleResolver放进容器中，该自动配置就不生效了，
	 * *       所以容器中用的是我们自己配置的 这个方法名要叫localeResolver，其他的测试没用，估计是根据id来取bean的
	 *  @Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(prefix = "spring.mvc", name = "locale")
		public LocaleResolver localeResolver() {
			if (this.mvcProperties
					.getLocaleResolver() == WebMvcProperties.LocaleResolver.FIXED) {
				return new FixedLocaleResolver(this.mvcProperties.getLocale());
			}
			AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
			localeResolver.setDefaultLocale(this.mvcProperties.getLocale());
			return localeResolver;
		}
	 * */
	@Bean
	public LocaleResolver localeResolver() {
		return new MyLocalResolve();
	}
	
	@Bean
	public FlashMapManager flashMapManager() {
		
	    return new AbstractFlashMapManager() {
			
			private final String FLASH_MAPS_SESSION_ATTRIBUTE = SessionFlashMapManager.class.getName() + ".FLASH_MAPS";

			@Override
			protected List<FlashMap> retrieveFlashMaps(HttpServletRequest request) {
				HttpSession session = request.getSession(false);
				@SuppressWarnings("unchecked")
				List<FlashMap> list = (session != null ? (List<FlashMap>) session.getAttribute(FLASH_MAPS_SESSION_ATTRIBUTE) : null);
	    		if(list!=null) {
	    			Iterator<FlashMap> it = list.iterator();
	    			while(it.hasNext()) {
	    				FlashMap map = it.next();
	    				log.info("获取retrieveFlashMaps:{}",map);
	    			}
	    		}else {
	    			log.info("获取retrieveFlashMaps:null");
	    		}
				return list;
			}

			@Override
			protected void updateFlashMaps(List<FlashMap> flashMaps, HttpServletRequest request,
					HttpServletResponse response) {
				if(flashMaps!=null) {
					//int i = 1/0;
					for(FlashMap map : flashMaps) {
						log.info("保存updateFlashMaps:{}",map);
					}
				}else {
					log.info("获取updateFlashMaps:null");
				}
				WebUtils.setSessionAttribute(request, FLASH_MAPS_SESSION_ATTRIBUTE, (!flashMaps.isEmpty() ? flashMaps : null));
			}
	    };
	}
}
