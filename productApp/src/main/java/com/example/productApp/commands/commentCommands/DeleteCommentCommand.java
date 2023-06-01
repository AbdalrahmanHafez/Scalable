package com.example.productApp.commands.commentCommands;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import java.util.HashMap;

@Component
public class DeleteCommentCommand extends CommentCommand{
    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        return getService().deleteComment((String)map.get("comment_id"));
    }
}

