package com.example.productApp.messageQueue;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductListeners {

    //private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @KafkaListener(
            topics = "productApp" ,
            groupId = "product"
    )
    void listener(String data){
        //logsSender.sendLogMessage(data);
        System.out.println("Listener received " + data );
    }

}
