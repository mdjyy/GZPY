package com.gzpy;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableRabbit
public class GzpyManagerApplication {
    //主程序
	public static void main(String[] args) {
		SpringApplication.run(GzpyManagerApplication.class, args);
	}

}
