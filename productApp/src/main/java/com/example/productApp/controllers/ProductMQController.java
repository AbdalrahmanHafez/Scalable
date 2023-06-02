package com.example.productApp.controllers;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.example.productApp.messageQueue.MessageRequest;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/productsMessages")
public class ProductMQController {

    public static boolean isPaused = false;
    private final KafkaTemplate<String , Map<String ,Object>> kafkaTemplate;

    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public ProductMQController(KafkaTemplate<String, Map<String ,Object>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }
//    @PostMapping
//    public void publish(MessageRequest request){
//        Map<String , Object> map = new HashMap<>();
//        map.put(request.message().keySet().toString() ,request.message().values());
//        kafkaTemplate.send("productApp", map);
//    }

    public void setLogLevel(String new_log_level) {
        log.info("[INFO] setting loglevel to " + new_log_level);

        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.valueOf(new_log_level));
    }
    public void setDbConnectionCount(Integer count) {
        log.info("[INFO] trying to set db connections count to " + count);

        // TODO: dynamic db connection string

        // String connectionString = System.getProperty("spring.data.mongodb.uri");
        // ConnectionString connectionString = new ConnectionString(new_connection_uri);
        ConnectionString connectionString = new ConnectionString(
                "mongodb+srv://Yousef:OrY39uo9XYO9FS4k@cluster0.urbm3.mongodb.net/?retryWrites=true&w=majority&maxPoolSize="
                        + count);

        MongoClient newClient = MongoClients.create(connectionString);

        mongoClient = newClient;
        mongoTemplate = new MongoTemplate(newClient, "mediadb");

        log.info("[INFO] db connections is updated to " + count);
    }

    public void setThreadCount(Integer thread_count) {
        try {
            if (thread_count < 1) {
                log.error("[ERROR] thread_count is less than 1");
                return;
            }

            threadPoolTaskExecutor.setCorePoolSize(thread_count);
            threadPoolTaskExecutor.setMaxPoolSize(thread_count);
            threadPoolTaskExecutor.setQueueCapacity(thread_count);

            log.info(String.format("[INFO] Thread count is set to %d", thread_count));
        } catch (Exception e) {
            log.error("[ERROR]" + e.getMessage());
        }
    }
    @KafkaListener(topics = "product-controller", groupId = "product_commands_group")
    public void controller_handler(Map<String, Object> data) {
        // TODO: make each into command pattern
        System.out.println("[Controller Handler] Received data: " + data.toString());

        String request_action_name = (String) data.get("Action");
        if (request_action_name == null) {
            log.error("[ERROR] request_action_name is null");
            return;
        }

        switch (request_action_name) {
            case "set_error_reporting_level":
                String newLogLevel = (String) data.get("reportinglevel");
                if (newLogLevel == null) {
                    log.error("[ERROR] reportinglevel is null");
                    return;
                }
                setLogLevel(newLogLevel);
                break;

            case "freeze":
                isPaused = true;
                log.info("[INFO] Server is paused");
                break;

            case "continue":
                isPaused = false;
                log.info("[INFO] Server is continued");
                break;

            case "set_max_thread_count":
                Integer new_thread_count = 0;
                try {
                    new_thread_count = Integer.parseInt(data.get("threads").toString());
                } catch (Exception e) {
                    log.error("[ERROR] threads value is incorrect");
                }

                setThreadCount(new_thread_count);
                break;

            case "set_max_db_connections_count":
                Integer new_db_connections_count = 0;
                try {
                    new_db_connections_count = Integer.parseInt(data.get("dbconnections").toString());
                } catch (Exception e) {
                    log.error("[ERROR] dbconnections value is incorrect");
                }
                setDbConnectionCount(new_db_connections_count);
                break;

            default:
                log.error("[ERROR] request_action_name is not valid");
                return;
        }

        log.info("[Controller Handler] Done");
    }
}
