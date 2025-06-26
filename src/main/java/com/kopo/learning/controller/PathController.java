package com.kopo.learning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PathController {
    
    @GetMapping("/path")
    public String path() {
        return "path";
    }
    
    // '나만의 경로' 추천 기능
} 