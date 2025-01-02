package net.javaguides.springboot_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.javaguides.springboot_backend.exception.ResourceNotFoundException;
import net.javaguides.springboot_backend.model.Employee;
import net.javaguides.springboot_backend.repo.EmployeeRepo;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    
    @Autowired
    private EmployeeRepo employeeRepo; 
    
    // Get all employees
    @GetMapping("")
    public List<Employee> getAllEmployees(){
        return employeeRepo.findAll();
    }
    
    // Create a new employee
    @PostMapping("/create")
    public Employee createEmployee(@RequestBody Employee employee) {
        System.out.println("Received: " + employee); // Log incoming employee
        return employeeRepo.save(employee);
    }

    // Get employee by id
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    	 System.out.println("Fetching employee with ID: " + id);
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));
        return ResponseEntity.ok(employee);
    }
    
    // Update employee by id
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        // Fetch existing employee
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));
        
        // Update fields
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmailId(employeeDetails.getEmailId());
        
        // Save updated employee
        Employee updatedEmployee = employeeRepo.save(employee);
        
        // Create response object with custom message and status
        return ResponseEntity.ok().body(
            new ApiResponse("Employee updated successfully", updatedEmployee)
        );
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable Long id) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));

        employeeRepo.delete(employee);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee deleted successfully!");
        return ResponseEntity.ok(response);
    }

    // Custom response class to send a message along with the updated employee details
    public static class ApiResponse {
        private String message;
        private Employee employee;

        public ApiResponse(String message, Employee employee) {
            this.message = message;
            this.employee = employee;
        }

        public String getMessage() {
            return message;
        }

        public Employee getEmployee() {
            return employee;
        }
    }
}
