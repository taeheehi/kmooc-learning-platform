package com.kopo.learning;

import com.kopo.learning.common.vo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserAdvice {
    @ModelAttribute("user")
    public User addUserToModel(HttpSession session) {
        return (User) session.getAttribute("user");
    }
} 