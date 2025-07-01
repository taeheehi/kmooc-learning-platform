package com.kopo.learning.community.review;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import com.kopo.learning.common.vo.User;

@Controller
@RequestMapping("/community")
public class ReviewController {

    @GetMapping("/review")
    public String review() {
        return "community/review";
    }

    @GetMapping("/review/write")
    public String writeForm() {
        return "community/review_write";
    }

    @PostMapping("/review/write")
    public String write(@RequestParam String title, @RequestParam String content, @SessionAttribute(name = "user", required = false) User user) {
        // TODO: 실제 저장 로직 필요 (예: 서비스 호출)
        // 임시로 저장 없이 목록으로 리다이렉트
        return "redirect:/community/review";
    }
} 