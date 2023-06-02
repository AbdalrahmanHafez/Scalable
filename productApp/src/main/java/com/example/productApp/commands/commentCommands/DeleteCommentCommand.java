package com.example.productApp.commands.commentCommands;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeleteCommentCommand extends CommentCommand{
    @Override
    public Object execute(HashMap<String, Object> map) {
        return getService().deleteComment((String)map.get("comment_id"));
    }
}

