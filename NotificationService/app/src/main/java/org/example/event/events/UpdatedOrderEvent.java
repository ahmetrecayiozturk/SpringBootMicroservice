package org.example.event.events;

import org.example.event.event_generic.AppEvent;
import org.example.model.Order;

// Order update edilmesi eventini oluşturalum, bu event oluştuğunda birazdan yazacağım service'de bu eventi dinleyip ona göre bir işlem yapacağız
public class UpdatedOrderEvent implements AppEvent<Order>{
    // order nesnesini tutalım
    private Order source;
    // default contructor oluşturalım, bu json için gerekli
    public UpdatedOrderEvent() {}

    public UpdatedOrderEvent(Order source) {
        this.source = source;
    }
    // AppEvent arayüzündeki getSource metodunu implement edelim
    @Override
    public Order getSource() {
        return source;
    }
}
