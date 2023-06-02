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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
public class ProductMQController {

    public static boolean isPaused = false;
    private final KafkaTemplate<String , Map<String ,Object>> kafkaTemplate;

    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
   String userToken;

    public ProductMQController(KafkaTemplate<String, Map<String ,Object>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }
    
//    @PostMapping("/test")
//    public String publish(@RequestBody Map<String , Object> request){
//        kafkaTemplate.send("productApp", request);
//        return "done";
//    }
    public void publishUserId(){
        HashMap<String , Object> request = new HashMap<>();
        request.put("getuserbytoken",userToken);
        request.put("request", "request");
        kafkaTemplate.send("user-product" , request);
        log.info("request sent to user");
    }
    public void logInhUserId(){
        Map<String , Object> request = new HashMap<>();
        Map<String,String> userData = new HashMap<String, String>() ;
        userData.put("email","newuser3@email.com");
        userData.put("password","user3@pass");
        request.put("login" , userData);
        request.put("request", "request");
        kafkaTemplate.send("user-product" , request);
        log.info("Token request");
    }
    @KafkaListener(
            topics = "user-product",
            groupId = "UserConsumerGroup"
    )
    void userListener(HashMap<String,Object> data){
      if (data.get("request")==null) {
          userToken = (String) data.get("login");
          log.info("Token received");
      }

    }
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

    public String setThreadPoolSize(int threadCount){
            if (threadCount > threadPoolTaskExecutor.getMaxPoolSize())
                return "This number exceeds the max pool size. Try a number less than " + threadPoolTaskExecutor.getMaxPoolSize();
            threadPoolTaskExecutor.setCorePoolSize(threadCount);
            log.info("New thread pool size: " + threadPoolTaskExecutor.getCorePoolSize());
            return "Thread pool size changed to: " + threadCount;
    }    @KafkaListener(topics = "product-controller", groupId = "product_commands_group")
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

                setThreadPoolSize(new_thread_count);
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
