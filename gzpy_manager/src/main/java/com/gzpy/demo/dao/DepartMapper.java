package com.gzpy.demo.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.gzpy.demo.po.Department;

@Mapper
public interface DepartMapper {
	@Select("select * from department where id = #{id}")
    public Department findDepartById(Integer id);
	
	/**
	 * Option设置一些选项，useGeneratedKeys=true将id封装到入参department中
	 * keyProperty指定主键
	 * */
	@Insert("insert into department(departmentName) values (#{departmentName})")
	@Options(useGeneratedKeys=true,keyProperty="id")
	public int addDepart(Department depart);
	
}
