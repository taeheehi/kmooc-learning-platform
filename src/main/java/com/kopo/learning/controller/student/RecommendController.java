package com.kopo.learning.controller.student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecommendController {
    
    @GetMapping("/recommend")
    public String recommend() {
        return "student/recommend";
    }
    
    // LLM 기반 강의 추천 기능
} 