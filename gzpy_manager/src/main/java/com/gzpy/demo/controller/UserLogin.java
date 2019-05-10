package com.gzpy.demo.controller;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzpy.demo.po.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/user")
@Controller
public class UserLogin {
	
    @PostMapping("/login")
	public String login(String username,String password,
			HttpSession session,Map<String,String> map,RedirectAttributes redirectAttributes) {
	    log.info("登陆：[username]:{},[password]:{}",username,password);
	   
	    if("root".equals(username)&&"123456".equals(password)) {
	    	User user = new User();
	    	user.setUserName(username);
            user.setPassword(password);
            session.setAttribute("myuser",user);
            //10s注销
        	session.setMaxInactiveInterval(10);
    	    redirectAttributes.addFlashAttribute("msg","hahah");
	    	return "redirect:/Head";
	    }
	    //map.put("msg", "登陆出错，请重新登陆");
	    redirectAttributes.addFlashAttribute("msg","登陆出错，请重新登陆");
	    Enumeration<String> e = session.getAttributeNames();
	    while(e.hasMoreElements()) {
	    	
	    	System.out.println(e.nextElement());
	    }
	    return "redirect:/login";
    }
}
