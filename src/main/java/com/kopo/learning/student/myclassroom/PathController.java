package com.kopo.learning.student.myclassroom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PathController {
    
    @GetMapping("/path")
    public String path() {
        return "student/path";
    }
    
    @GetMapping("/mycourses")
    public String myCourses() {
        return "student/mycourses";
    }
    
    // '나만의 경로' 추천 기능
} 