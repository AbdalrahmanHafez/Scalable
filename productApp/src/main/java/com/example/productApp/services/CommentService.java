package com.example.productApp.services;

import com.example.productApp.logs.logsSender;
import com.example.productApp.models.Comment;
import com.example.productApp.models.Product;
import com.example.productApp.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;



import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final  ProductService productService;
    private final Logger logger;

    @Autowired
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Autowired
    public CommentService(CommentRepository commentRepository, ProductService productService, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.commentRepository = commentRepository;
        this.productService = productService;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.logger = LoggerFactory.getLogger(CommentService.class);

    }

    public CompletableFuture<List<Comment>> getCommentsByUser(String userId) {

            CompletableFuture<List<Comment>> commentsList = CompletableFuture.supplyAsync(() -> {
                if (userId == null) {
                    String errorMessage = "UserId cannot be null";
//            logger.error(errorMessage);
                    logsSender.sendErrorMessage(errorMessage);
                }

                List<Comment> comments = commentRepository.findByUserId(userId);

                String logMessage;
                if (comments.isEmpty()) {
                    logMessage = "No comments found for UserId: " + userId;
//            logger.info(logMessage);
                } else {
                    logMessage = "Retrieved " + comments.size() + " comments for UserId: " + userId;
//            logger.info(logMessage);
                }
                logsSender.sendLogMessage(logMessage);

                return comments;
            }, threadPoolTaskExecutor);

            return commentsList;
    }


    public CompletableFuture<List<Comment>> getAppComments(String appId)  {
        CompletableFuture<List<Comment>> commentsList = CompletableFuture.supplyAsync(() -> {
            Product app = productService.getProductById(appId);
            if (app == null) {
                String errorMessage = "App not found with ID: " + appId;
//            logger.error(errorMessage);
                logsSender.sendErrorMessage(errorMessage);
            }

            List<Comment> comments = commentRepository.findByAppId(appId);

            String logMessage;
            if (comments.isEmpty()) {
                logMessage = "No comments found for AppId: " + appId;
            } else {
                logMessage = "Retrieved " + comments.size() + " comments for AppId: " + appId;
            }
//        logger.info(logMessage);
            logsSender.sendLogMessage(logMessage);

            return comments;
            }, threadPoolTaskExecutor);

        return commentsList;

    }


    public CompletableFuture<Comment> createComment(Comment comment){
        CompletableFuture<Comment> commentCF = CompletableFuture.supplyAsync(() -> {
            if (comment.getUserId() == null) {
                String errorMessage = "UserId required for creating comment";
//            logger.error(errorMessage);
                logsSender.sendErrorMessage(errorMessage);
            }
            if (comment.getComment() == null) {
                String errorMessage = "Content required for creating comment";
//            logger.error(errorMessage);
                logsSender.sendErrorMessage(errorMessage);
            }
            comment.setId(UUID.randomUUID().toString());
            String logMessage = "Comment created successfully. CommentId: " + comment.getId();
//        logger.info(logMessage);
            logsSender.sendLogMessage(logMessage);

            return commentRepository.save(comment);
            }, threadPoolTaskExecutor);

        return commentCF;

    }


    public CompletableFuture<String> deleteComment(String commentId)  {
        CompletableFuture<String> commentCF = CompletableFuture.supplyAsync(() -> {
            Comment comment = commentRepository.findById(commentId).orElse(null);
            if (comment == null) {
                //logs
                String errorMessage = "Comment not found with ID: " + commentId;
                logger.error(errorMessage);
                logsSender.sendErrorMessage(errorMessage);
            }

            commentRepository.deleteById(commentId);

            //logs
            String logMessage = "Comment deleted with ID: " + commentId;
            logger.info(logMessage);
            logsSender.sendLogMessage(logMessage);

            return "Comment deleted successfully";
        }, threadPoolTaskExecutor);

        return commentCF;

    }

}
