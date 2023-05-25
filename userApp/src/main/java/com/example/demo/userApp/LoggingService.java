package com.example.demo.userApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    public void logInfo(String message) {
        logger.info(message);
    }
    public void logInfo(String message, Long ID) {
        logger.info(message+" , UserID:"+ID);
    }

    public void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public void logError(String message, long ID) {
        logger.error(message+" , UserID:"+ID);
    }
    public void logError(String message) {
        logger.error(message);
    }
    public void logDebug(String message, Long ID) {
        logger.debug(message);
    }

    public void logWarning(String message) {
        logger.warn(message);
    }
}
