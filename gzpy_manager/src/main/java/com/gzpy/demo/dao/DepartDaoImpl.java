package com.gzpy.demo.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.gzpy.demo.po.Department;

public class DepartDaoImpl implements IDepartDao{
	
	 JdbcTemplate t;
	
    @Override
    public List<Department> queryDepart(int id) {
    	return null;
    }
}
