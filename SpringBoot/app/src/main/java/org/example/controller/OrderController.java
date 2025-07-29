package org.example.controller;

import org.aspectj.weaver.ast.Or;
import org.example.Json.JsonUtil;
import org.example.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.example.event.event_service.DomainEventPublisher;
import org.example.event.events.CreatedOrderEvent;
import org.example.event.events.UpdatedOrderEvent;
import org.example.generics.ApiResponse;
import org.example.model.Order;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    public OrderRepository orderRepository;

    public DomainEventPublisher publisher;

    private final KafkaTemplate<String,String> kafkaTemplate;

    public JsonUtil jsonUtil;

    public OrderController(OrderRepository orderRepository,DomainEventPublisher publisher, KafkaTemplate<String, String> kafkaTemplate, JsonUtil jsonUtil) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
        this.kafkaTemplate = kafkaTemplate;
        this.jsonUtil = jsonUtil;
    }

    @RequestMapping("/create")
    public ApiResponse<Order> createOrder(@RequestBody Order order) throws Exception {

        if (orderRepository.findByProductName(order.getProductName()).isPresent()) {
            throw new Exception("Order already exists...");
        }
        Order createdOrder = new Order();
        //Order'ı oluşturalım
        createdOrder.setProductName(order.getProductName());
        createdOrder.setQuantity(order.getQuantity());
        //Order'ı kaydedelim
        orderRepository.save(createdOrder);
        //Orderı güncelledik,şimdi bu güncellenen orderı bir event olarak publish edeceğiz,ondan önce return tipimize gerekli şeyleri set edelim
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Success");
        apiResponse.setSuccess(true);
        apiResponse.setData(createdOrder);
        //Eventimizi oluşturalım
        CreatedOrderEvent createdOrderEvent = new CreatedOrderEvent(order);
        //Önce Stringe çevirelim
        String eventJson = JsonUtil.toJson(createdOrderEvent);
        //Şimdi bu eventimizi kafka üzerinden publish edeceğiz
        kafkaTemplate.send("order-created-event", eventJson);
        //bu da eski event publish etme şeklimiz servisler arası değildi öyle gerçek hayattaki örneklerden uzak bir örnekti
        publisher.publish(createdOrderEvent);
        return apiResponse;
    }
    @RequestMapping("/update")
    public ApiResponse<Order> updateOrder(@RequestBody Order order) throws Exception {
        Order existOrder = orderRepository.findByProductName(order.getProductName()).orElseThrow();
        //Order'ı güncelleyelim
        existOrder.setQuantity(order.getQuantity());
        existOrder.setPrice(order.getPrice());
        //Orderı güncelledik,şimdi bu güncellenen orderı bir event olarak publish edeceğiz,ondan önce return tipimize gerekli şeyleri set edelim
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Success");
        apiResponse.setSuccess(true);
        apiResponse.setData(existOrder);
        //Order'ı kaydedelim
        orderRepository.save(existOrder);
        //Eventimizi oluşturalım
        UpdatedOrderEvent updatedOrderEvent = new UpdatedOrderEvent(order);
        //Önce Stringe çevirelim
        String eventJson = JsonUtil.toJson(updatedOrderEvent);
        //Şimdi bu eventimizi kafka üzerinden publish edeceğiz
        kafkaTemplate.send("order-updated-event", eventJson);
        //bu da eski event publish etme şeklimiz servisler arası değildi öyle gerçek hayattaki örneklerden uzak bir örnekti
        publisher.publish(updatedOrderEvent);
        return apiResponse;
    }
}
