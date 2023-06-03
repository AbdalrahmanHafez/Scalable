package com.example.productApp.controllers;

import com.example.productApp.models.Comment;
import com.example.productApp.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/product/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/user/{userId}")
    public CompletableFuture<List<Comment>> getCommentsByUser(@PathVariable String userId) throws Exception {
        return commentService.getCommentsByUser(userId);
    }

    @GetMapping("/app/{appId}")
    public CompletableFuture<List<Comment>> getCommentsByApp(@PathVariable String appId) throws Exception {
        return commentService.getAppComments(appId);
    }

    @PostMapping
    public CompletableFuture<Comment> createComment(@RequestBody Comment comment) throws Exception {
        return commentService.createComment(comment);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable String commentId) throws Exception {
        commentService.deleteComment(commentId);
    }
}
