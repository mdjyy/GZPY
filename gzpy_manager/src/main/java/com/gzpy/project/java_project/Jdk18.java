package com.gzpy.project.java_project;

import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.util.ByteUtils;

import com.gzpy.demo.po.Employee;

public class Jdk18 {
	static CacheKeyPrefix simple() {
		return name -> name + "::";
	}
	public static void main(String[] args) {
		System.out.println(simple().compute("Emp"));
		SerializationPair pair = SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer(Object.class));
	    Employee e = new Employee();
	    e.setId(1);
	    e.setLastName("哈哈");
		System.out.println(new String(ByteUtils.getBytes(pair.getWriter().write(e))));
	}
}
