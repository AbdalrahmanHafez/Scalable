package com.example.productApp.messageQueue;

import java.util.Map;

public record MessageRequest(Map<String , Object> message) {
}
