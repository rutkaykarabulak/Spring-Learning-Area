package com.example.payroll;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.payroll.CommonTypes.OrderStatus;

@RestController
public class OrderController {
    private OrderRepository orderRepository;
    private OrderModelAssembler assembler;
    public OrderController(OrderRepository repo, OrderModelAssembler assembler) {
        this.orderRepository = repo;
        this.assembler = assembler;
    }

    //#region APIs
    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id));

        EntityModel<Order> entityModel = 
        assembler.toModel(order);

        return ResponseEntity.ok().body(entityModel);
    }

    @GetMapping("/order")
    public ResponseEntity<?> getOrders() {
        List<EntityModel<Order>> orders = 
        orderRepository.findAll().stream().map(
            o -> assembler.toModel(o)
        ).toList();

        return ResponseEntity.ok().body(orders);
    }

    @PostMapping("/order")
    public ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order newOrder) {
        EntityModel<Order> entityModel = assembler.toModel(orderRepository.save(newOrder));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<?> updateOrder(@RequestBody Order newOrder, @PathVariable Long id) {
        Order updatedOrder = orderRepository.findById(id)
        .map(o -> {
            o.setDescription(newOrder.getDescription());
            o.setStatus(newOrder.getStatus());
            return orderRepository.save(o);
        }).orElseGet(
            () -> {
                newOrder.setId(id);
                return orderRepository.save(newOrder);
            }
        );
        
        EntityModel<Order> entityModel = assembler.toModel(updatedOrder);

        return ResponseEntity.ok().body(entityModel);
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/order/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        Order cancelledOrder = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if (cancelledOrder.getStatus() == OrderStatus.IN_PROGRESS) {
            cancelledOrder.setStatus(OrderStatus.CANCELLED);
            return ResponseEntity.ok().body(assembler.toModel(orderRepository.save(cancelledOrder)));
        }

        return ResponseEntity
        .status(HttpStatus.METHOD_NOT_ALLOWED)//
        .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
        .body(org.springframework.hateoas.mediatype.problem.Problem
        .create()
        .withTitle("Method not allowed")
        .withDetail("You can not cancel the order when its in " + cancelledOrder.getStatus() + " " +" status"));
    }

    @PutMapping("/order/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {

        Order completedOrder = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        if (completedOrder.getStatus() == OrderStatus.IN_PROGRESS) {
            completedOrder.setStatus(OrderStatus.COMPLETED);
            return ResponseEntity.ok().body(assembler.toModel(orderRepository.save(completedOrder)));
        }

        return ResponseEntity
        .status(HttpStatus.METHOD_NOT_ALLOWED)//
        .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
        .body(org.springframework.hateoas.mediatype.problem.Problem
        .create()
        .withTitle("Method not allowed")
        .withDetail("You can not cancel the order when its in " + completedOrder.getStatus() + " " +" status"));
    }

    //#endregion
}
