package th.co.priorsolution.training.restaurant.controller.rest;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.training.restaurant.entity.OrderEntity;
import th.co.priorsolution.training.restaurant.model.CustomerOrderDtoModel;
import th.co.priorsolution.training.restaurant.model.OrderStatusDtoModel;
import th.co.priorsolution.training.restaurant.model.ResponseModel;
import th.co.priorsolution.training.restaurant.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderRestController {

    private OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderEntity> createOrder(@Valid @RequestBody CustomerOrderDtoModel orderDto) {
        OrderEntity order = this.orderService.createOrder(orderDto);
        return ResponseEntity.status(201).body(order);
    }

    @GetMapping("/status/{orderId}")
    public OrderStatusDtoModel getOrderStatus(
            @PathVariable Long orderId
    ){
        return this.orderService.getOrderStatus(orderId);
    }
}
