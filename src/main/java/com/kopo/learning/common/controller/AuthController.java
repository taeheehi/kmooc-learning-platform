package com.kopo.learning.common.controller;

import com.kopo.learning.common.vo.User;
import com.kopo.learning.admin.user.UserService;
import com.kopo.learning.admin.user.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/login")
    public String login() {
        return "common/login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String id,
                            @RequestParam String password,
                            HttpSession session) {
        try {
            User user = userService.loginUser(id, password);
            if (user != null) {
                session.setAttribute("user", user);
                session.removeAttribute("loginError");
                return "redirect:/";
            } else {
                session.setAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
                return "redirect:/login";
            }
        } catch (RuntimeException e) {
            session.setAttribute("loginError", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String register() {
        return "common/register";
    }

    @PostMapping("/register")
    public String registerPost(@RequestParam String id,
                               @RequestParam String name,
                               @RequestParam String password,
                               @RequestParam String passwordCheck,
                               @RequestParam String role,
                               @RequestParam String phone,
                               @RequestParam String email,
                               @RequestParam(required = false) String gender,
                               @RequestParam(required = false) String birth,
                               Model model) {
        if (!password.equals(passwordCheck)) {
            model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
            return "common/register";
        }
        if (userService.loginUser(id, password) != null) {
            model.addAttribute("msg", "이미 존재하는 아이디입니다.");
            return "common/register";
        }
        if (phone == null || phone.trim().isEmpty()) {
            model.addAttribute("msg", "핸드폰번호는 필수 입력입니다.");
            return "common/register";
        }
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("msg", "이메일은 필수 입력입니다.");
            return "common/register";
        }
        if (userMapper.existsByPhone(phone)) {
            model.addAttribute("msg", "이미 등록된 핸드폰 번호입니다.");
            return "common/register";
        }
        if (userMapper.existsByEmail(email)) {
            model.addAttribute("msg", "이미 등록된 이메일입니다.");
            return "common/register";
        }
        try {
            userService.registerUser(id, name, password, role, phone, email, gender, birth);
        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
            return "common/register";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
} 