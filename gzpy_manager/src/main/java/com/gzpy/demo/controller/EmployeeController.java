package com.gzpy.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.gzpy.demo.po.Employee;
import com.gzpy.demo.service.EmployeeService;

@RestController
public class EmployeeController {
	@Autowired
    EmployeeService service;
	
	@GetMapping(value="/Emp/{id}")
	public Employee getEmployee(@PathVariable Integer id) {
		System.out.println("---------id"+id);
		return service.getEmployee(id);
	}
}
