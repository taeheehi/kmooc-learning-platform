package com.kopo.learning.student.courselist;

import com.kopo.learning.common.vo.KmoocCourse;
import com.kopo.learning.student.courselist.KmoocCourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Controller
public class CourseCatalogController {

    @Autowired
    private KmoocCourseMapper kmoocCourseMapper;

    @GetMapping("/courses")
    public String courses(Model model,
                          @RequestParam(name = "keyword", required = false) String keyword,
                          @RequestParam(name = "category", required = false) String category) {

        // 복수 카테고리 지원
        List<String> selectedCategories = new ArrayList<>();
        if (category != null && !category.isEmpty()) {
            selectedCategories = Arrays.asList(category.split(","));
        }

        List<KmoocCourse> courses;
        if (StringUtils.hasText(keyword) && !selectedCategories.isEmpty()) {
            courses = kmoocCourseMapper.findByCourseNameContainingIgnoreCaseAndCategoryIn(keyword, selectedCategories);
        } else if (StringUtils.hasText(keyword)) {
            courses = kmoocCourseMapper.findByCourseNameContainingIgnoreCase(keyword);
        } else if (!selectedCategories.isEmpty()) {
            courses = kmoocCourseMapper.findByCategoryIn(selectedCategories);
        } else {
            courses = kmoocCourseMapper.findAll();
        }

        List<String> categories = kmoocCourseMapper.findDistinctCategories();
        // 카테고리별 강의 개수 계산
        Map<String, Long> categoryCounts = new LinkedHashMap<>();
        for (String cat : categories) {
            categoryCounts.put(cat, kmoocCourseMapper.countByCategory(cat));
        }

        model.addAttribute("courses", courses);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryCounts", categoryCounts);
        model.addAttribute("selectedKeyword", keyword);
        model.addAttribute("selectedCategory", category);

        return "student/courses";
    }
} 