package com.example.payroll;

import java.util.Objects;

import org.aspectj.weaver.ast.Instanceof;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Employee {
    
    //#region Private fields
    private @Id @GeneratedValue Long id;
    private String name;
    private String role;    
    //#endregion

    //#region Constructors
    Employee() {

    } // default constructor

    Employee(String name, String role) {
        this.name = name;
        this.role = role;
    } // parameterized constructor
    //#endregion

    //#region Getters
    public final String getName() {
        return this.name;
    }

    public final String getRole() {
        return this.role;
    }

    public final Long getId() {
        return this.id;
    }
    //#endregion

    //#region Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setId(Long id) {
        this.id = id;
    }
    //#endregion

    //#region toString, equals, hashCode overriding

    @Override
    public String toString() {
        return "Employee{" + "id=" + this.id + ", name=" + this.name + 
        ", role=" + this.role  + "'\'";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.role);
    }

      @Override
    public boolean equals(Object o) {
        if (this == o)
        return true;
        if (!(o instanceof Employee))
        return false;
        Employee employee = (Employee) o;
        return Objects.equals(this.id, employee.id) && Objects.equals(this.name, employee.name)
            && Objects.equals(this.role, employee.role);
    }
    //#endregion



}
