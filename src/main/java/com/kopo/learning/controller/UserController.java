package com.kopo.learning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    
    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }
    
    // 사용자 정보(마이페이지) 관련 기능
} 