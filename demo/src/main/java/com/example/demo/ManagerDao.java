package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerDao extends JpaRepository<Manager, Long> {

	public Manager findByEmailAndPassword(String email,String password);
	
	public Manager findByEmail(String email);
}
