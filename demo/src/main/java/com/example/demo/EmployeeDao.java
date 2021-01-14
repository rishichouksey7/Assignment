package com.example.demo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
public interface EmployeeDao extends CrudRepository<Employee, Long> {

	public List<Employee> findByManager(Manager manager);
	
	public Employee findByEmailId(String emailId);
}
