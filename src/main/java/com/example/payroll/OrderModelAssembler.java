package com.example.payroll;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>>{
    @Override
    public EntityModel<Order> toModel(Order order) {
        return EntityModel.of(order, 
        linkTo(methodOn(OrderController.class).getOrder(order.getId())).withSelfRel(),
        linkTo(methodOn(OrderController.class).getOrders()).withRel("orders"));
    }
}
