package com.example.productApp.kafka;

import com.google.gson.Gson;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(
            topics = "productApp" ,
            groupId = "product"
    )
    void listener(String data){
        //ApiRequest api = new Gson().fromJson(data, ApiRequest.class);
        System.out.println("Listener received " + data );
    }

}
