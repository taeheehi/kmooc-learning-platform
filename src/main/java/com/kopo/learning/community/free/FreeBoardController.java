package com.kopo.learning.community.free;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/community")
public class FreeBoardController {

    @GetMapping("/board")
    public String freeBoard() {
        return "community/board";
    }
} 