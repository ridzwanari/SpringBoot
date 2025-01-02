package net.javaguides.springboot_backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot_backend.model.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository <Employee,Long> {
	
	
	

}
