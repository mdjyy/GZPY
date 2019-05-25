package com.gzpy.demo.service;

import com.gzpy.demo.po.Employee;

public interface EmployeeService {
	
    public Employee getEmployee(Integer id);
    
    public int addEmployee(Employee e);
    
    public int deleteEmployee(Integer id);
    
    public int updateEmployee(Employee e);
}
