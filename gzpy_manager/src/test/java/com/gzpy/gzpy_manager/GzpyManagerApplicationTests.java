package com.gzpy.gzpy_manager;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gzpy.tomcat.TomcatServerNio;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GzpyManagerApplicationTests {

	@Test
	public void contextLoads() {
	}
    public static void main(String[] args) {
		URL url = TomcatServerNio.class.getClassLoader().getResource("Index.html");
	    System.out.println(url.getPath());
		System.out.println(System.getProperty("user.dir"));
    }
}
