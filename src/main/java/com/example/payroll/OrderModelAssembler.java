package com.example.payroll;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.payroll.CommonTypes.OrderStatus;

import org.springframework.hateoas.EntityModel;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>>{
    @Override
    public EntityModel<Order> toModel(Order order) {
        EntityModel<Order> orderEntity = EntityModel.of(order, 
        linkTo(methodOn(OrderController.class).getOrder(order.getId())).withSelfRel(),
        linkTo(methodOn(OrderController.class).getOrders()).withRel("orders"));

        if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            orderEntity.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
            orderEntity.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
        }

        return orderEntity;
    }
}
