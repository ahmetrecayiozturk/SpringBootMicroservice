package org.example.Json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.event.events.UpdatedOrderEvent;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Json'a çevirme fonksiyonumuzu yazalım
    public static String toJson(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        try {
            // objeyi direkt string'e çeviriyoruz
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw e;
        }
    }

    // statik olarak tanımladık, böylece her yerde nesne üretmeden kullanacağız
    public static ObjectMapper objectMApper() {
        return objectMapper;
    }

}
