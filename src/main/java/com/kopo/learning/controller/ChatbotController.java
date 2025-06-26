package com.kopo.learning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatbotController {
    
    @GetMapping("/chatbot")
    public String chatbot() {
        return "chatbot";
    }
    
    // 챗봇 관련 기능
} 