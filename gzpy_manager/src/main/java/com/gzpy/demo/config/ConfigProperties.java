package com.gzpy.demo.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.http.HttpProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gzpy.demo.po.Dog;
import com.gzpy.demo.po.Person;
/**
 * 类似于之前的<beans id = getPerson ></beans>这种值注入
 * */
@Configuration
@EnableConfigurationProperties(HttpProperties.class)
public class ConfigProperties {
	public ConfigProperties(HttpProperties p) {
       System.out.println(p.getEncoding());
	}
    @Bean
	public Person getPerson() {
		Person person = new Person();
        Dog dog = new Dog();
        dog.setAge(11);
        dog.setName("狗");
		person.setDog(dog);
		person.setList(new ArrayList());
		Map map = new HashMap();
		map.put("k1", "11");
		person.setMap(map);
		return person;
	}
	
}
