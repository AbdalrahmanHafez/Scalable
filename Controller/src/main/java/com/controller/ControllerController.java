package com.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/controller")
@Slf4j
public class ControllerController {
    private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    private Invoker invoker;

    @GetMapping("/testlogs")
    public String help(){
        log.info("logged info");
        log.warn("logged Warn");
        log.error("logged Error");
        log.debug("logged Debug");

        return "logging";
    }
    @PostMapping("/command")
    public String publish(@RequestBody Map<String, Object> request){
        String actionName = (String) request.get("Action");
        try {
            Class CommandClass = Class.forName("com.controller."+actionName+"Command");
            Constructor<CommandExecutor> constructor = CommandClass.getConstructor(KafkaTemplate.class);
            CommandExecutor command = constructor.newInstance(kafkaTemplate);
            invoker.SetCommand(command);
            invoker.ExecuteCommand(request);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return "Success";
    }
}
