package main.java.com.example.productApp.controllers;

import main.java.com.example.productApp.models.Comment;
import main.java.com.example.productApp.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

//    @GetMapping("/user/{userId}")
//    public List<Comment> getCommentsByUser(@PathVariable String userId) {
//        return commentService.getCommentsByUser(userId);
//    }
//
//    @GetMapping("/app/{appId}")
//    public List<Comment> getCommentsByApp(@PathVariable String appId) {
//        return commentService.getCommentsByApp(appId);
//    }
//
//    @PostMapping
//    public Comment createComment(@RequestBody Comment comment) {
//        return commentService.createComment(comment);
//    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId);
    }
}
