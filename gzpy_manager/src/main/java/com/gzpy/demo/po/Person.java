package com.gzpy.demo.po;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *  dog:
        name: haha
        age: 16
    list:
        - 10
        - 20
    map: {k1: 1, k2: 2}
 * */
//@ConfigurationProperties(prefix="person")
//@Component
public class Person {
    private Dog dog;
    private List list;
    private Map map;
	public Dog getDog() {
		return dog;
	}
	public void setDog(Dog dog) {
		this.dog = dog;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	@Override
	public String toString() {
		return "Person [dog=" + dog + ", list=" + list + ", map=" + map + "]";
	}
    
}
