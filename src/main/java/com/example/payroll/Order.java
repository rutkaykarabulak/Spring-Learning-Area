package com.example.payroll;

import java.util.Objects;

import com.example.payroll.CommonTypes.OrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
@Entity
@Table(name = "CUSTOMER_ORDER")
public class Order {
    private @Id @GeneratedValue Long id;
    private String description;
    private OrderStatus status;

    Order() {};

    Order(String description, OrderStatus status) {
        this.description = description;
        this.status = status;
    }

    //#region Getters
    public Long getId() {
        return this.id;
    }
    public String getDescription() {
        return this.description;
    }

    public OrderStatus getStatus() {
        return this.status;
    }
    //#endregion

    //#region Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    //#endregion

    //#region Overriding metohds

    @Override
    public String toString() {
        return "Order{" + "id=" + this.id + ", description='" + this.description + '\'' + ", status=" + this.status + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
        return true;
        if (!(o instanceof Order))
        return false;
        Order order = (Order) o;
        return Objects.equals(this.id, order.id) && Objects.equals(this.description, order.description)
            && this.status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.description, this.status);
    }
    //#endregion


    
}
