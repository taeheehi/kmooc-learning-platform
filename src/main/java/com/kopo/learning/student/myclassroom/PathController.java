package com.kopo.learning.student.myclassroom;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PathController {
    
    @GetMapping("/path")
    public String path() {
        return "redirect:/learning-space";
    }
    
    @GetMapping("/mycourses")
    public String myCourses() {
        return "redirect:/learning-space";
    }
    
    // 새로운 통합된 '내 학습 공간' 페이지
    @GetMapping("/learning-space")
    public String learningSpace(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // 추후 model에 데이터 추가 가능
        return "student/learning-space";
    }
    
    // '나만의 경로' 추천 기능
} 