package com.example.productApp.commands.commentCommands;

import com.example.productApp.models.Comment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreateCommentCommand extends CommentCommand{
    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        Comment comment = new Comment();
        comment.setAppId((String)map.get("app_id"));
        comment.setComment((String)map.get("comment"));
        comment.setUserId((String)map.get("user_id"));
        return getService().createComment(comment);
    }
}