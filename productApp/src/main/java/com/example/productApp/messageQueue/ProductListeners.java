package com.example.productApp.messageQueue;

import com.example.productApp.commands.Command;
import com.example.productApp.commands.Invoker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Constructor;
import java.util.HashMap;

@Component
@Slf4j
public class ProductListeners {


    private final Invoker invoker;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProductListeners(Invoker invoker, KafkaTemplate<String, String> kafkaTemplate) {
        this.invoker = invoker;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
            topics = "productApp",
            groupId = "product"
    )
    void listener(String data) {
        log.info(data);
        System.out.println("Listener received " + data);
    }

    @KafkaListener(
            topics = "product-controller",
            groupId = ""
    )
    void listener(@RequestBody HashMap<String, Object> request) {
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
}
