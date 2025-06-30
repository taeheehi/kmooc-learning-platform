package com.kopo.learning.community.notice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
    
    @GetMapping("/notice")
    public String notice() {
        return "community/notice";
    }
    
    @GetMapping("/qna")
    public String qna() {
        return "community/qna";
    }
    
    @GetMapping("/review")
    public String review() {
        return "community/review";
    }
    
    @GetMapping("/board")
    public String board() {
        return "community/board";
    }
    
    // 게시판 관련 기능 (질문, 후기, 자유게시판 등)
} 