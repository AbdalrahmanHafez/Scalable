package com.example.demo.userApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    private static final Logger info = LoggerFactory.getLogger("info");
    private static final Logger error = LoggerFactory.getLogger("error");

    public void logInfo(String message) {
        info.info(message);
    }
    public void logInfo(String message, Long ID) {
        info.info(message+" , UserID:"+ID);
    }
    public void logError(String message, Long ID) {
        error.error(message+" , UserID:"+ID);
    }
    public void logError(String message) {
        error.error(message);
    }

    public void logError(String message, Throwable throwable) {
        error.error(message, throwable);
    }
}

