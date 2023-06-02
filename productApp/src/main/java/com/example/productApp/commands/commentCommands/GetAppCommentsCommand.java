package com.example.productApp.commands.commentCommands;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GetAppCommentsCommand extends CommentCommand {

    @Override
    public Object execute(HashMap<String, Object> map){
        return getService().getAppComments((String) map.get("app_id"));
    }
}