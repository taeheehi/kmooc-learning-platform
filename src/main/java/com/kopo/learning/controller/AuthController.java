package com.kopo.learning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // 로그아웃은 보통 POST로 처리하지만, 기본 구조를 위해 GetMapping으로 둡니다.
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
} 