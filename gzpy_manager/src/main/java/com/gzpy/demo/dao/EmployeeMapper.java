package com.gzpy.demo.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gzpy.demo.po.Employee;

@Mapper
public interface EmployeeMapper {
	
    public Employee queryEmployeeById(Integer id);
    
    public int addEmployee(Employee employee);
    
    public int deleteEmployee(Integer id);
    
    public int updateEmployee(Employee e);

}
