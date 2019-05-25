package com.gzpy.demo.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EmployeeShedule {

	/**
	 * s s  s   s       s  s 
	 * 秒 分  时 一个月中的日      月   一星期中的日
	 * */
	@Scheduled(cron="0 * * * * 1-7")
	public void schedule() {
    	System.out.println("定时任务开始");
    }
}
