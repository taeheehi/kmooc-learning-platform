package com.kopo.learning.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    
    @GetMapping("/admin/users")
    public String adminUsers() {
        return "admin/admin_users";
    }
    
    @GetMapping("/admin/stats")
    public String adminStats() {
        return "admin/admin_stats";
    }
    
    // 관리자 관련 기능 (회원관리, 공지사항 관리, 통계 등)
} 