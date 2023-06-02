package com.example.productApp.interceptors;

import com.example.productApp.controllers.ProductMQController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class CustomInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Pre-processing logic
        // For example, you can perform authentication or modify the request

        // if (request.getRequestURI().equals("/continue")) {
        // System.out.println("[INFO] Prehandle Continue request");
        // return true;
        // }

        boolean canContinue = !ProductMQController.isPaused;
        System.out.println("[INFO] Prehandle " + (canContinue ? "continue" : "Blocked") + " " + request.getRequestURI()
                + " " + request.getMethod());

        return canContinue;
        // return true; // Proceed with the request handling
    }
}
