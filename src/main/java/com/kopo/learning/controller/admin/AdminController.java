package com.kopo.learning.controller.admin;

import com.kopo.learning.model.User;
import com.kopo.learning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AdminController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/admin/users")
    public String adminUsers(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("users", userList);
        return "admin/admin_users";
    }
    
    @GetMapping("/admin/stats")
    public String adminStats() {
        return "admin/admin_stats";
    }

    @PostMapping("/admin/users/toggle-lock")
    public String toggleLock(@RequestParam("userId") String userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setAccountNonLocked(!user.isAccountNonLocked());
            if (user.isAccountNonLocked()) {
                user.setLoginFailCount(0);
                user.setLockTime(null);
            } else {
                user.setLockTime(LocalDateTime.now());
            }
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/update-memo")
    public String updateMemo(@RequestParam("userId") String userId, @RequestParam("memo") String memo) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setAdminMemo(memo);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }
    
    // 관리자 관련 기능 (회원관리, 공지사항 관리, 통계 등)
} 