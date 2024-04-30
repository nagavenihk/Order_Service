package com.springmicro.order.service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springmicro.order.service.dto.OrderResponseDTO;
import com.springmicro.order.service.entity.Order;
import com.springmicro.order.service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    //validation
    //logging
    //exception handling
    @PostMapping
    public String placeNewOrder(@RequestBody Order order){
        return orderService.placeAnOrder(order);
    }
    @GetMapping("/{orderId}")
    public OrderResponseDTO getOrder(@PathVariable String orderId){
        return orderService.getOrder(orderId);
    }

}
