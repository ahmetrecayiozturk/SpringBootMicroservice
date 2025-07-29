package org.example.kafka_consumer.order;

import lombok.extern.slf4j.Slf4j;
import org.example.Json.JsonUtil;
import org.example.event.events.CreatedOrderEvent;
import org.example.event.events.UpdatedOrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

//burada ne yapıyoruz, burası localde çalışan kafka serverindaki eventleri dinleyecek ve eğer gelen eventleri topic'lerine göre yakalayacak ve ilgili topik
//methodunda ne varsa onu yapacak
@Slf4j//bu loglama için kullanılıyor
@Component
public class OrderEventConsumer {
    //kafka listener kullanıyoruz bu içine yazdığımız topiğe göre otomatik olarak eventleri yakalıyor
    @KafkaListener(topics = "order-created-event", groupId = "notification-group")
    public void consumeOrderCreatedEvent(String eventJson){
        try{
            //Önce gelen json'u evente çeviriyoruz
            CreatedOrderEvent createdOrderEvent = JsonUtil.objectMApper().readValue(eventJson, CreatedOrderEvent.class);
            //Sonra da bu eventi logluyoruz
            log.info("Order Created Event received: {}", createdOrderEvent);
            //print te edelim ne olur ne olmaz
            System.out.println("Order Created Event received: " + createdOrderEvent);
        }
        catch(Exception e){
            //eğer bir hata gerçekleşirse hem loglayalım hem de print edelim
            log.error("Error consuming OrderCreatedEvent: {}", eventJson, e);
            System.err.println("Error consuming OrderCreatedEvent: " + eventJson + ", Error: " + e.getMessage());
        }
    }
    //kafka listener kullanıyoruz bu içine yazdığımız topiğe göre otomatik olarak eventleri yakalıyor
    @KafkaListener(topics = "order-updated-event", groupId = "notification-group")
    public void consumeOrderUpdatedEvent(String eventJson){
        try{
            //Önce gelen json'u evente çeviriyoruz
            UpdatedOrderEvent updatedOrderEvent = JsonUtil.objectMApper().readValue(eventJson, UpdatedOrderEvent.class);
            //Sonra da bu eventi logluyoruz
            log.info("Order Updated Event received: {}", updatedOrderEvent);
            //print te edelim ne olur ne olmaz
            System.out.println("Order Updated Event received: " + updatedOrderEvent);
        }
        catch(Exception e){
            //eğer bir hata gerçekleşirse hem loglayalım hem de print edelim
            log.error("Error consuming OrderUpdatedEvent: {}", eventJson, e);
            System.err.println("Error consuming OrderUpdatedEvent: " + eventJson + ", Error: " + e.getMessage());
        }
    }
}
