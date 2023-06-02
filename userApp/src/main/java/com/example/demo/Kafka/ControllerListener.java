    package com.example.demo.Kafka;

    import com.example.demo.Commands.CommandDispatcher;
    import com.example.demo.Services.UserService;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.kafka.annotation.KafkaListener;
    import org.springframework.stereotype.Service;

    import java.util.HashMap;

    @Service
    public class ControllerListener {
        private static final Logger LOGGER = LoggerFactory.getLogger(ControllerListener.class);
        private final UserService userService;

        private final CommandDispatcher commandDispatcher;

        @Autowired
        public ControllerListener(UserService userService, CommandDispatcher commandDispatcher) {
            this.userService = userService;
            this.commandDispatcher = commandDispatcher;
        }

        @KafkaListener(topics = "${mq.address}", groupId = "UserConsumerGroup")
        public void consume(HashMap<String, Object> message) {
            if(message.get("response") == null) {
                LOGGER.info(String.format("Message received -> %s", message.toString()));
                commandDispatcher.executeMessage(message);
            }
            else{
                LOGGER.info(String.format("read my response, Message received -> %s", message.toString()));
            }
        }
    }
