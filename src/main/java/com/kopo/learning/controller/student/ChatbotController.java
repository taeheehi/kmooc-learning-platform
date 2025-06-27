package com.kopo.learning.controller.student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatbotController {
    
    @GetMapping("/chatbot")
    public String chatbot() {
        return "student/chatbot";
    }
    
    // 챗봇 관련 기능
} 