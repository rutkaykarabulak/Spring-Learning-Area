package com.example.payroll;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
public class EmployeeController {
    private final EmployeeRepository employeeRepo;

    private final EmployeeModelAssembler assembler;
    
    EmployeeController(EmployeeRepository repo, EmployeeModelAssembler assembler) {
        this.employeeRepo = repo;
        this.assembler = assembler;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    // Returns all the employees in the database
    @GetMapping("/employee")
    public CollectionModel<EntityModel<Employee>> getEmployees() {
        List<EntityModel<Employee>> employees = employeeRepo.findAll().stream()
        .map(e -> assembler.toModel(e)).toList();

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getEmployees()).withSelfRel());
    }
    // end:: get-aggregate-root

    // Gets the employee with given id. Single item.
    @GetMapping("/employee/{id}")
    public EntityModel<Employee> getEmployee(@PathVariable Long id) {
        Employee employee = employeeRepo.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
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
        employeeRepo.deleteById(id);
    }
}
