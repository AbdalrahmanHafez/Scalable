package com.example.productApp.commands.commentCommands;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
<<<<<<< Updated upstream
public class GetAppCommentsCommand extends CommentCommand {

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        return getService().deleteComment((String)map.get("app_id"));
=======
public class GetAppCommentsCommand extends CommentCommand{
    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        return getService().getAppComments((String)map.get("app_id"));
>>>>>>> Stashed changes
    }
}