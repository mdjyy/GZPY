package com.gzpy.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.stereotype.Service;

import com.gzpy.demo.dao.EmployeeMapper;
import com.gzpy.demo.po.Employee;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	EmployeeMapper mapper;
	@Autowired
	RedisTemplate<Object,Object> Emp;
	
	@Cacheable(cacheNames="Emp",key="#id",cacheManager="cacheManager")
	@Override
	public Employee getEmployee(Integer id) {
		Employee emp = mapper.queryEmployeeById(id);
		return emp;
	}

	@Override
	public int addEmployee(Employee e) {
		return mapper.addEmployee(e);
	}

	@Override
	public int deleteEmployee(Integer id) {
		return mapper.deleteEmployee(id);
	}

	@Override
	public int updateEmployee(Employee e) {
		return mapper.updateEmployee(e);
	}
    
}
