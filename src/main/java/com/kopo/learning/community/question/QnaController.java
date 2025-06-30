package com.kopo.learning.community.question;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/community")
public class QnaController {

    @GetMapping("/qna")
    public String qna() {
        return "community/qna";
    }
} 