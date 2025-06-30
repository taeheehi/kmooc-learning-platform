package com.kopo.learning.community.review;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/community")
public class ReviewController {

    @GetMapping("/review")
    public String review() {
        return "community/review";
    }
} 