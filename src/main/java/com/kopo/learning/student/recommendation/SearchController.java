package com.kopo.learning.student.recommendation;

import com.kopo.learning.common.vo.KmoocCourse;
import com.kopo.learning.student.courselist.KmoocCourseMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SearchController {

    private final KmoocCourseMapper kmoocCourseMapper;

    public SearchController(KmoocCourseMapper kmoocCourseMapper) {
        this.kmoocCourseMapper = kmoocCourseMapper;
    }

    @GetMapping("/search")
    public String search(Model model) {
        List<KmoocCourse> courses = kmoocCourseMapper.findAll();
        model.addAttribute("courses", courses);
        return "student/search";
    }
    
    // LLM 기반 강의 추천 기능
} 