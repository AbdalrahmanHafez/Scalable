package com.example.demo.Commands;

import com.example.demo.Kafka.ControllerProducer;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.Services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Service
@Configuration
public class CommandDispatcher {
   // private final Map<String, Command> possibleCommands;
   @Autowired
   private List<Command> commands;
   @Autowired
   private ControllerProducer controllerProducer;

    @Autowired
    private UserService userService;

    public CommandDispatcher() {
    }

    public void executeMessage(HashMap<String,Object> message) {
        Set<String> messagekeys = message.keySet();
        HashMap<String, Object> response = new HashMap<>();
        response.put("response","response");
        CommandInvoker commandInvoker = new CommandInvoker(null);
        for (String key: messagekeys)
        {
            for (Command c: commands)
            {
                System.out.println(c.getName());
                if(key.equals(c.getName())) {
                    commandInvoker.setCommand(c);
                        Object o = commandInvoker.execute(message.get(key));
                        response.put(c.getName(), o);
                        break;

                }
            }
        }

        controllerProducer.sendMessage(response);
    }

}
