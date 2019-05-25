package com.gzpy.demo.dao;

import java.util.List;

import com.gzpy.demo.po.Department;

public interface IDepartDao {
    public List<Department> queryDepart(int id);
}
