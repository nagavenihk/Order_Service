package com.springmicro.order.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springmicro.order.service.dto.OrderResponseDTO;
import com.springmicro.order.service.dto.PaymentDTO;
import com.springmicro.order.service.dto.UserDTO;
import com.springmicro.order.service.entity.Order;
import com.springmicro.order.service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;
    @Value("${paytm.producer.topic.name}")
    private String topicName;
    @Autowired
    private RestTemplate restTemplate;
    //place an order
    //get an order
    public String placeAnOrder(Order order) {
        //save a copy of order-service DB
        order.setPurchaseDate(new Date());
        order.setOrderId(UUID.randomUUID().toString().split("-")[0]);
        orderRepository.save(order);

        //send it to payment service using kafka
        try{
            kafkaTemplate.send(topicName,new ObjectMapper().writeValueAsString(order));
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        return "Your order with ("+order.getOrderId()+") has been placed ! we will notify once it will confirm";

    }

    public OrderResponseDTO getOrder(String orderId){
        //own DB -> ORDER
        Order order=orderRepository.findByOrderId(orderId);
        //PAYMENT -> REST call payment-service
        PaymentDTO paymentDTO = restTemplate.getForObject("http://localhost:9292/payment/" + orderId, PaymentDTO.class);
        //user-info -> rest call user-service
        UserDTO userDTO = restTemplate.getForObject("http://localhost:9393/users/" + order.getUserId(), UserDTO.class);

        return  OrderResponseDTO.builder()
                .order(order)
                .paymentResponse(paymentDTO)
                .userInfo(userDTO)
                .build();

    }
}