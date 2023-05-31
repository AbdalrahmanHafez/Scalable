package com.example.productApp.commands.commentCommands;

import com.example.productApp.models.Comment;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CreateCommentCommand extends CommentCommand {


    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        Comment comment = new Comment();
        comment.setAppId((String) map.get("app_id"));
        comment.setComment((String) map.get("comment"));
        comment.setUserId((String) map.get("user_id"));
        return getService().createComment(comment);
    }
}
