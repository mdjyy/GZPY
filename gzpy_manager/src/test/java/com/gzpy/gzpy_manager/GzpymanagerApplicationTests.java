package com.gzpy.gzpy_manager;

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
	@Autowired 
	Person person;
	@Test
	public void contextLoads() {
       System.out.println(person);
	}
}
