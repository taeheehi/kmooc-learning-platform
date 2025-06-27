package com.kopo.learning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.kopo.learning.User;
import com.kopo.learning.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";
        User user = userRepository.findById(sessionUser.getId());
        model.addAttribute("user", user);
        return "mypage";
    }

    @PostMapping("/mypage")
    public String updateMypage(@RequestParam String name,
                               @RequestParam String oldPassword,
                               @RequestParam String newPassword,
                               @RequestParam String phone,
                               @RequestParam String email,
                               @RequestParam(required = false) String gender,
                               @RequestParam(required = false) String birth,
                               HttpSession session,
                               Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";
        User user = userRepository.findById(sessionUser.getId());
        if (user == null) return "redirect:/login";
        // 기존 비밀번호 확인
        if (!user.getPassword().equals(oldPassword)) {
            model.addAttribute("msg", "기존 비밀번호가 일치하지 않습니다.");
            model.addAttribute("user", user);
            return "mypage";
        }
        String phoneNormalized = (phone == null) ? null : phone.replaceAll("-", "");
        // 본인 제외 중복 체크
        if (phoneNormalized != null && !phoneNormalized.equals(user.getPhone()) && userRepository.existsByPhone(phoneNormalized)) {
            model.addAttribute("msg", "이미 등록된 핸드폰 번호입니다.");
            model.addAttribute("user", user);
            return "mypage";
        }
        if (email != null && !email.equals(user.getEmail()) && userRepository.existsByEmail(email)) {
            model.addAttribute("msg", "이미 등록된 이메일입니다.");
            model.addAttribute("user", user);
            return "mypage";
        }
        user.setName(name);
        user.setPhone(phoneNormalized);
        user.setEmail(email);
        if (gender != null) user.setGender(gender);
        if (birth != null && !birth.isEmpty()) user.setBirth(LocalDate.parse(birth));
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(newPassword);
        }
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        session.setAttribute("user", user);
        model.addAttribute("msg", "정보가 성공적으로 수정되었습니다.");
        model.addAttribute("user", user);
        return "mypage";
    }

    // 이메일 인증 요청
    @PostMapping("/request-email-verify")
    public String requestEmailVerify(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";
        User user = userRepository.findById(sessionUser.getId());
        if (user == null) return "redirect:/login";
        String token = UUID.randomUUID().toString();
        user.setEmailVerifyToken(token);
        userRepository.save(user);
        // 실제 이메일 발송 대신 콘솔에 링크 출력
        System.out.println("[이메일 인증 링크] http://localhost:8085/verify-email?token=" + token);
        model.addAttribute("emailMsg", "이메일로 인증 링크가 발송되었습니다. (테스트: 콘솔에서 확인)");
        model.addAttribute("user", user);
        return "mypage";
    }

    // 이메일 인증 확인
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, HttpSession session, Model model) {
        User user = userRepository.findAll().stream()
            .filter(u -> token.equals(u.getEmailVerifyToken()))
            .findFirst().orElse(null);
        if (user == null) {
            model.addAttribute("emailMsg", "잘못된 인증 링크입니다.");
            return "mypage";
        }
        user.setEmailVerified(true);
        user.setEmailVerifyToken(null);
        userRepository.save(user);
        session.setAttribute("user", user);
        model.addAttribute("emailMsg", "이메일 인증이 완료되었습니다.");
        model.addAttribute("user", user);
        return "mypage";
    }

    // 프로필 이미지 업로드
    @PostMapping("/upload-profile-img")
    public String uploadProfileImg(@RequestParam("profileImg") MultipartFile file, HttpSession session, Model model) throws Exception {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";
        User user = userRepository.findById(sessionUser.getId());
        if (user == null) return "redirect:/login";
        if (!file.isEmpty()) {
            String uploadDir = "profile-uploads";
            Files.createDirectories(Paths.get(uploadDir));
            String filename = user.getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);
            file.transferTo(filePath);
            user.setProfileImg(filename);
            userRepository.save(user);
            session.setAttribute("user", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("msg", "프로필 이미지가 업로드되었습니다.");
        return "mypage";
    }

    // 프로필 이미지 조회
    @GetMapping("/profile-img/{filename}")
    public ResponseEntity<Resource> serveProfileImg(@PathVariable String filename) throws Exception {
        Path filePath = Paths.get("profile-uploads").resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        String contentType = Files.probeContentType(filePath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                .body(resource);
    }
    
    // 사용자 정보(마이페이지) 관련 기능
} 