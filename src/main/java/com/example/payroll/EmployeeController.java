package com.example.payroll;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.support.Repositories;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    private final EmployeeRepository employeeRepo;
    
    EmployeeController(EmployeeRepository repo) {
        this.employeeRepo = repo;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    // Returns all the employees in the database
    @GetMapping("/employee")
    public List<Employee> getEmployees() {
        return employeeRepo.findAll();
    }
    // end:: get-aggregate-root

    // Gets the employee with given id. Single item.
    @GetMapping("/employee/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeRepo.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PostMapping("/employee")
    public Employee newEmployee(@RequestBody Employee newEmployee) {
        return employeeRepo.save(newEmployee);
    }

    @PutMapping("/employee/{id}")
    public Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Employee employee = employeeRepo.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException(id));

        employee.setName(newEmployee.getName());
        employee.setRole(newEmployee.getRole());

        return employeeRepo.save(employee);
    }

    @DeleteMapping("/employee/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        try {
            employeeRepo.deleteById(id);
        } catch (EmployeeNotFoundException exception) {
            throw new EmployeeNotFoundException(id);
        }
    }
}
