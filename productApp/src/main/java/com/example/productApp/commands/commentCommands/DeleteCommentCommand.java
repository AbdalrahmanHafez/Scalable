package com.example.productApp.commands.commentCommands;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DeleteCommentCommand extends CommentCommand {


    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        return getService().deleteComment((String)map.get("comment_id"));
    }
}

