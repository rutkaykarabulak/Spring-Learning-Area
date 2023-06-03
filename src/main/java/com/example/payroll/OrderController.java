package com.example.payroll;

import java.util.List;

import org.apache.catalina.connector.Response;
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

    //#endregion
}
