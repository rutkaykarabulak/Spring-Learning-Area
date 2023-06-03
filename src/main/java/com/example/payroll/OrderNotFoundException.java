package com.example.payroll;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long id) {
        super("Order can not be found " + id);
    }
}
