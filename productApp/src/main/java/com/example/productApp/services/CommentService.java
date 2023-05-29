package com.example.productApp.services;

import com.example.productApp.logs.logsSender;
import com.example.productApp.models.Comment;
import com.example.productApp.models.Product;
import com.example.productApp.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;


import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final  ProductService productService;
    private final Logger logger;


    @Autowired
    public CommentService(CommentRepository commentRepository, ProductService productService) {
        this.commentRepository = commentRepository;
        this.productService = productService;
        this.logger = LoggerFactory.getLogger(CommentService.class);

    }

    public List<Comment> getCommentsByUser(String userId) throws Exception {
        if (userId == null) {
            String errorMessage = "UserId cannot be null";
            logger.error(errorMessage);
            logsSender.sendErrorMessage(errorMessage);
            throw new Exception(errorMessage);
        }

        List<Comment> comments = commentRepository.findByUserId(userId);

        if (comments.isEmpty()) {
            String logMessage = "No comments found for UserId: " + userId;
            logger.info(logMessage);
            logsSender.sendLogMessage(logMessage);
        } else {
            String logMessage = "Retrieved " + comments.size() + " comments for UserId: " + userId;
            logger.info(logMessage);
            logsSender.sendLogMessage(logMessage);
        }

        return comments;
    }


    public List<Comment> getAppComments(String appId) throws Exception {
        Product app = productService.getProductById(appId);
        if (app == null) {
            String errorMessage = "App not found with ID: " + appId;
            logger.error(errorMessage);
            logsSender.sendErrorMessage(errorMessage);
            throw new Exception(errorMessage);
        }

        List<Comment> comments = commentRepository.findByAppId(appId);

        String logMessage;
        if (comments.isEmpty()) {
            logMessage = "No comments found for AppId: " + appId;
        } else {
            logMessage = "Retrieved " + comments.size() + " comments for AppId: " + appId;
        }
        logger.info(logMessage);
        logsSender.sendLogMessage(logMessage);

        return comments;
    }


    public Comment createComment(Comment comment) throws Exception {
        if (comment.getUserId() == null) {
            String errorMessage = "UserId required for creating comment";
            logger.error(errorMessage);
            logsSender.sendErrorMessage(errorMessage);
            throw new Exception(errorMessage);
        }
        if (comment.getComment() == null) {
            String errorMessage = "Content required for creating comment";
            logger.error(errorMessage);
            logsSender.sendErrorMessage(errorMessage);
            throw new Exception(errorMessage);
        }
        comment.setId(UUID.randomUUID().toString());
        return commentRepository.save(comment);
    }


    public String deleteComment(String commentId) throws Exception {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            //logs
            String errorMessage = "Comment not found with ID: " + commentId;
            logger.error(errorMessage);
            logsSender.sendErrorMessage(errorMessage);
            throw new Exception(errorMessage);
        }

        commentRepository.deleteById(commentId);

        //logs
        String logMessage = "Comment deleted with ID: " + commentId;
        logger.info(logMessage);
        logsSender.sendLogMessage(logMessage);

        return "Comment deleted successfully";

    }

}

