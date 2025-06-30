package com.kopo.learning.admin.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.kopo.learning.common.vo.User;
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
import com.kopo.learning.admin.user.UserService;
import com.kopo.learning.admin.user.UserMapper;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";
        User user = userMapper.findById(sessionUser.getId());
        model.addAttribute("user", user);
        return "common/mypage";
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
        User user = userMapper.findById(sessionUser.getId());
        if (user == null) return "redirect:/login";
        User updated = userService.updateUser(user, name, oldPassword, newPassword, phone, email, gender, birth);
        if (updated == null) {
            model.addAttribute("msg", "\uae30\uc874 \ube44\ubc00\ubc88\ud638\uac00 \uc77c\uce58\ud558\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4.");
            model.addAttribute("user", user);
            return "mypage";
        }
        session.setAttribute("user", updated);
        model.addAttribute("msg", "\uc815\ubcf4\uac00 \uc131\uacf5\uc801\uc73c\ub85c \uc218\uc815\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
        model.addAttribute("user", updated);
        return "mypage";
    }

    // 이메일 인증 요청
    @PostMapping("/request-email-verify")
    public String requestEmailVerify(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";
        User user = userMapper.findById(sessionUser.getId());
        if (user == null) return "redirect:/login";
        String token = UUID.randomUUID().toString();
        user.setEmailVerifyToken(token);
        userMapper.save(user);
        // 실제 이메일 발송 대신 콘솔에 링크 출력
        System.out.println("[이메일 인증 링크] http://localhost:8085/verify-email?token=" + token);
        model.addAttribute("emailMsg", "이메일로 인증 링크가 발송되었습니다. (테스트: 콘솔에서 확인)");
        model.addAttribute("user", user);
        return "mypage";
    }

    // 이메일 인증 확인
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, HttpSession session, Model model) {
        User user = userMapper.findAll().stream()
            .filter(u -> token.equals(u.getEmailVerifyToken()))
            .findFirst().orElse(null);
        if (user == null) {
            model.addAttribute("emailMsg", "잘못된 인증 링크입니다.");
            return "mypage";
        }
        user.setEmailVerified(true);
        user.setEmailVerifyToken(null);
        userMapper.save(user);
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
        User user = userMapper.findById(sessionUser.getId());
        if (user == null) return "redirect:/login";
        if (!file.isEmpty()) {
            String uploadDir = "profile-uploads";
            Files.createDirectories(Paths.get(uploadDir));
            String filename = user.getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);
            file.transferTo(filePath);
            user.setProfileImg(filename);
            userMapper.save(user);
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
} 