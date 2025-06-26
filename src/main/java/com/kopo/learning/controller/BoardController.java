package com.kopo.learning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
    
    @GetMapping("/notice")
    public String notice() {
        return "notice";
    }
    
    @GetMapping("/qna")
    public String qna() {
        return "qna";
    }
    
    @GetMapping("/review")
    public String review() {
        return "review";
    }
    
    @GetMapping("/board")
    public String board() {
        return "board";
    }
    
    // 게시판 관련 기능 (질문, 후기, 자유게시판 등)
} 