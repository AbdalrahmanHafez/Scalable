package com.example.productApp.commands;

import com.example.productApp.commands.commentCommands.CommentCommand;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Invoker {
    private Command commExec;
    public void SetCommand(Command comm){
        commExec = comm;
    }
    public void ExecuteCommand(HashMap<String, Object> request) throws Exception {
        commExec.execute(request);
    }

}

