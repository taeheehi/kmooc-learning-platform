package com.kopo.learning.instructor.manage;

import com.kopo.learning.common.vo.User;
import com.kopo.learning.instructor.InstructorService;
import com.kopo.learning.common.vo.InstructorCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@Controller
public class CourseController {
    
    @Autowired
    private InstructorService instructorService;
    
    // @GetMapping("/mycourses")
    // public String myCourses() {
    //     return "student/mycourses";
    // }
    
    // 강의 등록 페이지
    @GetMapping("/course/add")
    public String courseAdd(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"INSTRUCTOR".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) {
            return "redirect:/login";
        }
        return "instructor/course_add";
    }
    
    // 강의 등록 처리
    @PostMapping("/course/add")
    public String courseAddProcess(@ModelAttribute InstructorCourse course, 
                                  HttpSession session, 
                                  Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"INSTRUCTOR".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) {
            return "redirect:/login";
        }
        
        // 관리자가 강의를 등록하는 경우 instructorId를 설정하지 않음 (나중에 강의자 배정)
        if (!"ADMIN".equals(user.getRole())) {
            course.setInstructorId(user.getId());
        }
        boolean success = instructorService.createCourse(course);
        
        if (success) {
            return "redirect:/course/manage?msg=success";
        } else {
            model.addAttribute("error", "강의 등록에 실패했습니다.");
            return "instructor/course_add";
        }
    }
    
    // 강의 관리 페이지
    @GetMapping("/course/manage")
    public String courseManage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"INSTRUCTOR".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) {
            return "redirect:/login";
        }
        
        // 강의 조회 (관리자는 모든 강의, 강의자는 자신의 강의만)
        List<InstructorCourse> courses;
        Map<String, Object> stats;
        
        if ("ADMIN".equals(user.getRole())) {
            courses = instructorService.getAllCourses();
            stats = instructorService.getAdminDashboardStats();
        } else {
            courses = instructorService.getInstructorCourses(user.getId());
            stats = instructorService.getInstructorDashboardStats(user.getId());
        }
        
        model.addAttribute("courses", courses);
        model.addAttribute("stats", stats);
        
        return "instructor/course_manage";
    }
    
    // 강의 수정 페이지
    @GetMapping("/course/edit/{id}")
    public String courseEdit(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"INSTRUCTOR".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) {
            return "redirect:/login";
        }
        
        InstructorCourse course = instructorService.getCourseById(id);
        if (course == null || (!"ADMIN".equals(user.getRole()) && !course.getInstructorId().equals(user.getId()))) {
            return "redirect:/course/manage";
        }
        
        model.addAttribute("course", course);
        return "instructor/course_edit";
    }
    
    // 강의 수정 처리
    @PostMapping("/course/edit/{id}")
    public String courseEditProcess(@PathVariable Long id, 
                                   @ModelAttribute InstructorCourse course,
                                   HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"INSTRUCTOR".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) {
            return "redirect:/login";
        }
        
        course.setId(id);
        // 관리자가 아닌 경우에만 instructorId를 설정 (관리자는 기존 instructorId 유지)
        if (!"ADMIN".equals(user.getRole())) {
            course.setInstructorId(user.getId());
        }
        instructorService.updateCourse(course);
        
        return "redirect:/course/manage?msg=updated";
    }
    
    // 강의 삭제
    @PostMapping("/course/delete/{id}")
    public String courseDelete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"INSTRUCTOR".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) {
            return "redirect:/login";
        }
        
        InstructorCourse course = instructorService.getCourseById(id);
        if (course != null && ("ADMIN".equals(user.getRole()) || course.getInstructorId().equals(user.getId()))) {
            instructorService.deleteCourse(id);
        }
        
        return "redirect:/course/manage?msg=deleted";
    }
    
    // 강의 상태 변경
    @PostMapping("/course/status/{id}")
    public String courseStatusChange(@PathVariable Long id, 
                                    @RequestParam String status,
                                    HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"INSTRUCTOR".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) {
            return "redirect:/login";
        }
        
        InstructorCourse course = instructorService.getCourseById(id);
        if (course != null && ("ADMIN".equals(user.getRole()) || course.getInstructorId().equals(user.getId()))) {
            instructorService.updateCourseStatus(id, status);
        }
        
        return "redirect:/course/manage?msg=status_changed";
    }
    
    // 수강생 확인 페이지
    @GetMapping("/course/students")
    public String courseStudents(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"INSTRUCTOR".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) {
            return "redirect:/login";
        }
        // 강의 조회 (관리자는 모든 강의, 강의자는 자신의 강의만)
        List<InstructorCourse> courses;
        if ("ADMIN".equals(user.getRole())) {
            courses = instructorService.getAllCourses();
        } else {
            courses = instructorService.getInstructorCourses(user.getId());
        }
        model.addAttribute("courses", courses);
        // 총 수강생 수 계산
        int totalStudents = courses.stream()
            .mapToInt(c -> c.getEnrollmentCount() != null ? c.getEnrollmentCount() : 0)
            .sum();
        model.addAttribute("totalStudents", totalStudents);
        // 평균 평점 계산
        double avgRating = courses.stream()
            .filter(c -> c.getAverageRating() != null && c.getAverageRating() > 0)
            .mapToDouble(InstructorCourse::getAverageRating)
            .average()
            .orElse(0.0);
        model.addAttribute("avgRating", avgRating);
        return "instructor/course_students";
    }
    
    // 특정 강의의 수강생 목록
    @GetMapping("/course/students/{courseId}")
    public String courseStudentList(@PathVariable Long courseId, 
                                   HttpSession session, 
                                   Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || (!"INSTRUCTOR".equals(user.getRole()) && !"ADMIN".equals(user.getRole()))) {
            return "redirect:/login";
        }
        
        InstructorCourse course = instructorService.getCourseById(courseId);
        if (course == null || (!"ADMIN".equals(user.getRole()) && !course.getInstructorId().equals(user.getId()))) {
            return "redirect:/course/students";
        }
        
        // 강의별 수강생 통계
        Map<String, Object> stats = instructorService.getCourseEnrollmentStats(courseId);
        
        model.addAttribute("course", course);
        model.addAttribute("stats", stats);
        
        return "instructor/course_student_detail";
    }
    
    // 강의 관련 기능 (내 강의실, 강의 등록/관리 등)
} 