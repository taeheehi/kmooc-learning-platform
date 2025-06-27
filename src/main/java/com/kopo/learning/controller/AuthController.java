package com.kopo.learning.controller;

import com.kopo.learning.model.User;
import com.kopo.learning.repository.UserRepository;
import com.kopo.learning.service.UserService;
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
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String id,
                            @RequestParam String password,
                            HttpSession session) {
        User user = userService.loginUser(id, password);
        if (user != null) {
            session.setAttribute("user", user);
            session.removeAttribute("loginError");
            return "redirect:/";
        } else {
            session.setAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@RequestParam String id,
                               @RequestParam String name,
                               @RequestParam String password,
                               @RequestParam String passwordCheck,
                               @RequestParam String role,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String email,
                               @RequestParam(required = false) String gender,
                               @RequestParam(required = false) String birth,
                               Model model) {
        if (!password.equals(passwordCheck)) {
            model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
            return "register";
        }
        if (userService.loginUser(id, password) != null) {
            model.addAttribute("msg", "이미 존재하는 아이디입니다.");
            return "register";
        }
        if (phone != null && userService.loginUser(null, null) != null) {
            model.addAttribute("msg", "이미 등록된 핸드폰 번호입니다.");
            return "register";
        }
        if (email != null && userService.loginUser(null, null) != null) {
            model.addAttribute("msg", "이미 등록된 이메일입니다.");
            return "register";
        }
        userService.registerUser(id, name, password, role, phone, email, gender, birth);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
} 