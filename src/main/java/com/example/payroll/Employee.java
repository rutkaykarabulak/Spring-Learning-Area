package com.example.payroll;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Employee {
    
    //#region Private fields
    private @Id @GeneratedValue Long id;
    private String firstName;
    private String lastName;
    private String role;    
    //#endregion

    //#region Constructors
    Employee() {

    } // default constructor

    Employee(String firstName, String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    } // parameterized constructor
    //#endregion

    //#region Getters
    public final String getName() {
        return this.firstName + " " + this.lastName;
    }

    public final String getFirstName() {
        return this.firstName;
    }

    public final String getLastName() {
        return this.lastName;
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
        String[] names = name.split(" ");
        this.firstName = names[0];
        this.lastName = names[1];
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        return "Employee{" + "id=" + this.id + ", name=" + this.firstName + " " + this.lastName +  
        ", role=" + this.role  + "'\'";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.firstName, this.lastName, this.role);
    }

      @Override
    public boolean equals(Object o) {
        if (this == o)
        return true;
        if (!(o instanceof Employee))
        return false;
        Employee employee = (Employee) o;
        return Objects.equals(this.id, employee.id) && Objects.equals(this.firstName, employee.firstName)
            && Objects.equals(this.lastName, employee.lastName)
            && Objects.equals(this.role, employee.role);
    }
    //#endregion



}
