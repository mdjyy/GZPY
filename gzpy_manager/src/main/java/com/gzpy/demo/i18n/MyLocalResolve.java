package com.gzpy.demo.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyLocalResolve implements LocaleResolver {

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String lang = request.getParameter("l");
		Locale local = Locale.getDefault();
		if(!StringUtil.isNullOrEmpty(lang)) {
			String msg[] = lang.split("_");
			local = new Locale(msg[0], msg[1]);
		}
		log.info("国际化请国家{},语言{}",local.getCountry(),local.getLanguage());
		return local;
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		
	}
    
}
