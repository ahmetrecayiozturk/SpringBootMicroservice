package org.example.event.events;

import org.example.event.event_generic.AppEvent;
import org.example.model.Order;
import org.springframework.stereotype.Component;

// Order oluşması eventini oluşturalum, bu event oluştuğunda birazdan yazacağım service'de bu eventi dinleyip ona göre bir işlem yapacağız
public class CreatedOrderEvent implements AppEvent<Order> {
    private Order source; // <-- burada "source" olmalı!

    // default contructor oluşturalım, bu json için gerekli
    public CreatedOrderEvent() {}

    public CreatedOrderEvent(Order source) {
        this.source = source;
    }

    @Override
    public Order getSource() {
        return source;
    }
}
