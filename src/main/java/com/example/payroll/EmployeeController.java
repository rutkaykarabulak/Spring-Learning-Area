package com.example.payroll;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> newEmployee(@RequestBody Employee employee) {
        EntityModel<Employee> entityModel = assembler.toModel(employeeRepo.save(employee));

        return ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
        .body(entityModel);
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Employee updatedEmployee = employeeRepo.findById(id)
        .map(e -> {
            e.setFirstName(newEmployee.getFirstName());
            e.setLastName(newEmployee.getLastName());
            e.setRole(newEmployee.getRole());
            return employeeRepo.save(e);
        }) //
        .orElseGet(() -> {
            newEmployee.setId(id);
            return employeeRepo.save(newEmployee);
        });

        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);
        return ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {    
        employeeRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
