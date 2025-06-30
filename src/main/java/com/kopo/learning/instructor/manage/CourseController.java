package com.kopo.learning.instructor.manage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CourseController {
    
    // @GetMapping("/mycourses")
    // public String myCourses() {
    //     return "student/mycourses";
    // }
    
    @GetMapping("/course/add")
    public String courseAdd() {
        return "instructor/course_add";
    }
    
    @GetMapping("/course/manage")
    public String courseManage() {
        return "instructor/course_manage";
    }
    
    @GetMapping("/course/students")
    public String courseStudents() {
        return "instructor/course_students";
    }
    
    // 강의 관련 기능 (내 강의실, 강의 등록/관리 등)
} 