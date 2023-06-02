package com.example.productApp.messageQueue;

import com.example.productApp.commands.Command;
import com.example.productApp.commands.Invoker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class ProductListeners {


    private final Invoker invoker;
    private final KafkaTemplate<String, Map<String , Object>> kafkaTemplate;

    public ProductListeners(Invoker invoker, KafkaTemplate<String,  Map<String , Object>> kafkaTemplate) {
        this.invoker = invoker;
        this.kafkaTemplate = kafkaTemplate;
    }

//    @KafkaListener(
//            topics = "productApp",
//            groupId = "product"
//    )
//    void listener1(HashMap<String , Object> data) {
//        System.out.println("Listener received " + data.toString());
//    }

    @KafkaListener(
            topics = "product-controller-commands",
            groupId = "product-controller-commands-group"
    )
    void listener(HashMap<String, Object> request) {
        String actionName = request.toString();
        try {
            Class CommandClass = Class.forName("com.example.productApp.commands.controller." + actionName + "Command");
            Constructor<Command> constructor = CommandClass.getConstructor(KafkaTemplate.class);
            Command command = constructor.newInstance(kafkaTemplate);
            invoker.SetCommand(command);
            invoker.ExecuteCommand(request);
            log.info("Command executed successfully");
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
    }

    @KafkaListener(
            topics = "user-product",
            groupId = "UserConsumerGroup"
    )
    void userListener(HashMap<String,Object> request){
            String userId = (String) request.get("getuserbytoken");
            log.info("Received userId");
    }
}
