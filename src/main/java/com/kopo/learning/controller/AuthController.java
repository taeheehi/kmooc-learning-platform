package com.kopo.learning.controller;

import com.kopo.learning.User;
import com.kopo.learning.UserRepository;
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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String id,
                            @RequestParam String password,
                            HttpSession session) {
        User user = userRepository.findById(id);
        if (user != null && user.getPassword().equals(password)) {
            // 로그인 성공: 실패횟수 초기화, 마지막 로그인 시각 기록
            user.setLoginFailCount(0);
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
            session.setAttribute("user", user);
            session.removeAttribute("loginError");
            return "redirect:/";
        } else {
            // 로그인 실패: 실패횟수 증가
            if (user != null) {
                int fail = user.getLoginFailCount() == null ? 1 : user.getLoginFailCount() + 1;
                user.setLoginFailCount(fail);
                userRepository.save(user);
            }
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
        if (userRepository.findById(id) != null) {
            model.addAttribute("msg", "이미 존재하는 아이디입니다.");
            return "register";
        }
        String phoneNormalized = (phone == null) ? null : phone.replaceAll("-", "");
        if (phoneNormalized != null && userRepository.existsByPhone(phoneNormalized)) {
            model.addAttribute("msg", "이미 등록된 핸드폰 번호입니다.");
            return "register";
        }
        if (email != null && userRepository.existsByEmail(email)) {
            model.addAttribute("msg", "이미 등록된 이메일입니다.");
            return "register";
        }
        // 역할 한글→영어 변환
        String roleEng = role;
        switch (role) {
            case "학습자": roleEng = "STUDENT"; break;
            case "강의자": roleEng = "INSTRUCTOR"; break;
            case "관리자": roleEng = "ADMIN"; break;
        }
        // 핸드폰 번호 포맷팅 (010-1234-1234)
        String phoneFormatted = null;
        if (phone != null && !phone.isEmpty()) {
            String digits = phone.replaceAll("[^0-9]", "");
            if (digits.length() == 11 && digits.startsWith("010")) {
                phoneFormatted = String.format("010-%s-%s", digits.substring(3,7), digits.substring(7));
            } else {
                phoneFormatted = phone; // 예외: 11자리가 아니면 원본 저장
            }
        }
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPassword(password);
        user.setRole(roleEng);
        user.setPhone(phoneFormatted);
        user.setEmail(email);
        if (gender != null) user.setGender(gender);
        if (birth != null && !birth.isEmpty()) user.setBirth(LocalDate.parse(birth));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus("ACTIVE");
        user.setLoginFailCount(0);
        user.setEmailVerified(false);
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
} 