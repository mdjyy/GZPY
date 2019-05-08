package com.gzpy.gzpy_manager;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gzpy.demo.po.Person;
/**
 * 测试类也要在主程序包路径下面
 * */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GzpymanagerApplicationTests {
	
	@Test
	public void contextLoads() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        service.submit(new Callable<Object>() {
		    @Override
		    public Object call() throws Exception {
		    	return null;
		    }
        });
        
	}
	
	
}
